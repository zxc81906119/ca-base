package com.redhat.cleanbase.cache.resolver;

import com.redhat.cleanbase.cache.manager.condition.CacheManagerCondition;
import com.redhat.cleanbase.cache.manager.getter.CacheManagersGetter;
import com.redhat.cleanbase.common.spring.AppContext;
import com.redhat.cleanbase.common.type.DataWithId;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.*;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

@Slf4j
public class MultiCacheManagerCacheResolver implements CacheResolver {

    private final List<CacheManager> conditions;
    private final Map<String, Integer> cacheNameIndexMap;

    public MultiCacheManagerCacheResolver(CacheManagersGetter cacheManagersGetter) {
        val cacheManagers = cacheManagersGetter.get();
        if (CollectionUtils.isEmpty(cacheManagers)) {
            throw new RuntimeException("必須提供至少一個 cache manager condition");
        }
        this.conditions = cacheManagers;
        this.cacheNameIndexMap = toCacheNameIndexMap(cacheManagers);
    }

    private static void fillCaches(
            CacheOperationInvocationContext<?> cacheContext,
            CacheableOperation cacheableOperation,
            List<Cache> caches
    ) {
        if (caches.size() <= 1) {
            return;
        }

        val key = getCacheKey(cacheContext, cacheableOperation);
        if (key == null) {
            return;
        }

        val pendingCaches = new ArrayList<Cache>();
        for (Cache cache : caches) {
            val valueOpt = getCacheVal(cache, key);
            if (valueOpt.isEmpty()) {
                pendingCaches.add(cache);
                continue;
            }

            if (CollectionUtils.isEmpty(pendingCaches)) {
                continue;
            }

            for (Cache pendingCache : pendingCaches) {
                pendingCache.putIfAbsent(key, valueOpt.get());
            }

            pendingCaches.clear();
        }

    }

    private static Object getCacheKey(CacheOperationInvocationContext<?> cacheContext, CacheableOperation cacheableOperation) {
        val method = cacheContext.getMethod();
        val args = cacheContext.getArgs();
        val opKey = cacheableOperation.getKey();
        if (StringUtils.hasText(opKey)) {
            val evaluationContext = new MethodBasedEvaluationContext(
                    cacheableOperation,
                    method,
                    args,
                    new DefaultParameterNameDiscoverer()
            );
            val exp = new SpelExpressionParser().parseExpression(opKey);
            return exp.getValue(evaluationContext, String.class);
        }
        // ref: CacheAspectSupport
        val keyGenerator = getKeyGenerator(cacheableOperation.getKeyGenerator());
        return keyGenerator.generate(cacheContext.getTarget(), method, args);
    }

    private static KeyGenerator getKeyGenerator(String keyGeneratorStr) {
        if (StringUtils.hasText(keyGeneratorStr)) {
            return AppContext.getBean(KeyGenerator.class, keyGeneratorStr);
        }
        val beanOrNull = AppContext.getBeanOrNull(KeyGenerator.class);
        return Optional.ofNullable(beanOrNull)
                .orElseGet(SimpleKeyGenerator::new);
    }

    private static Optional<Object> getCacheVal(Cache cache, Object key) {
        try {
            return Optional.ofNullable(cache.get(key))
                    .map(Cache.ValueWrapper::get);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public @NonNull Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> context) {
        val operation = context.getOperation();
        val cacheNames = operation.getCacheNames();
        if (CollectionUtils.isEmpty(cacheNames)) {
            return List.of();
        }
        val caches = getCaches(cacheNames);
        if (operation instanceof CacheableOperation cacheableOperation) {
            fillCaches(context, cacheableOperation, caches);
        }
        return caches;
    }

    private List<Cache> getCaches(Set<String> cacheNames) {
        val priorityQueue = new PriorityQueue<>(Comparator.comparingInt(CacheWithIndex::id));
        for (String cacheName : cacheNames) {
            val index = getCacheManagerIndex(cacheName);
            if (index == null) {
                throw new RuntimeException("cache name not found in every cache manager !");
            }
            val cacheManager = conditions.get(index);
            val cache = Objects.requireNonNull(cacheManager.getCache(cacheName), "正常實做不會出現這個問題");
            priorityQueue.add(new CacheWithIndex(cache, index));
        }
        return priorityQueue.stream()
                .map(CacheWithIndex::data)
                .toList();
    }

    private Integer getCacheManagerIndex(String cacheName) {
        return cacheNameIndexMap.computeIfAbsent(
                cacheName,
                (name) ->
                        IntStream.range(0, conditions.size())
                                .filter((index) ->
                                        conditions.get(index) instanceof CacheManagerCondition condition
                                                && condition.isSupported(name)
                                )
                                .boxed()
                                .findFirst()
                                .orElse(null)
        );

    }

    private Map<String, Integer> toCacheNameIndexMap(@NonNull List<CacheManager> conditions) {
        val cacheNameIndexMap = new ConcurrentHashMap<String, Integer>();
        for (int i = 0; i < conditions.size(); i++) {
            val cacheManager = conditions.get(i);
            val cacheNames = cacheManager.getCacheNames();
            if (!CollectionUtils.isEmpty(cacheNames)) {
                for (String cacheName : cacheNames) {
                    if (cacheNameIndexMap.containsKey(cacheName)) {
                        throw new RuntimeException("不接受全域重複 cacheName: %s 註冊".formatted(cacheName));
                    }
                    cacheNameIndexMap.put(cacheName, i);
                }
            }
        }
        return cacheNameIndexMap;
    }

    public record CacheWithIndex(Cache data, Integer id) implements DataWithId<Cache, Integer> {
    }

}
