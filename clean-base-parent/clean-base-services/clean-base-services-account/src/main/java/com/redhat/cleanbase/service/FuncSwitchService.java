package com.redhat.cleanbase.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.redhat.cleanbase.config.prop.FuncSwitchConfigProp;
import com.redhat.cleanbase.repository.model.FuncSwitch;
import com.redhat.cleanbase.repository.FuncSwitchRepository;
import lombok.*;
import org.mapstruct.BeanMapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FuncSwitchService {
    public static final String FUNC_SWITCH_REDIS_KEY_PREFIX = "func-switch";

    private final RedisTemplate<String, List<FuncSwitchCache>> redisTemplate;

    private final FuncSwitchConfigProp funcSwitchConfigProp;

    private final FuncSwitchRepository funcSwitchRepository;

    public boolean isFuncEnabled(String funcName, String path) {
        return getFuncSwitchList(funcName).stream()
                .filter((funcSwitchCache) -> path.matches(funcSwitchCache.getPathExpression()))
                .findFirst()
                .map(FuncSwitchCache::getEnabled)
                .orElse(funcSwitchConfigProp.getDefaultEnabled());
    }

    public List<FuncSwitchCache> getFuncSwitchList(@NonNull String funcName, long timeoutMinutes, TimeUnit timeUnit) {
        val redisKey = getRedisKey(funcName);
        val stringListValueOperations = redisTemplate.opsForValue();
        val funcSwitchInfoCaches = stringListValueOperations.get(redisKey);
        if (funcSwitchInfoCaches != null) {
            return funcSwitchInfoCaches;
        }
        val funcSwitchList = funcSwitchRepository.findByFuncNameAndPathExpressionIsNotNull(funcName);
        val cacheList = Mapper.INSTANCE.toCacheList(funcSwitchList);
        val finalList = getFinalList(cacheList);
        stringListValueOperations.set(redisKey, finalList, timeoutMinutes, timeUnit);
        return finalList;
    }

    private static List<FuncSwitchCache> getFinalList(List<FuncSwitchCache> cacheList) {
        if (cacheList == null) {
            return new ArrayList<>();
        }
        if (cacheList.isEmpty()) {
            return cacheList;
        }
        return cacheList.stream()
                .sorted(
                        Comparator.comparing(
                                (funcSwitchCache) -> funcSwitchCache.getPathExpression().length()
                                , (a, b) -> b - a
                        )
                )
                .collect(Collectors.toList());
    }

    public List<FuncSwitchCache> getFuncSwitchList(@NonNull String funcName) {
        return getFuncSwitchList(
                funcName
                , funcSwitchConfigProp.getDefaultTtl()
                , funcSwitchConfigProp.getDefaultTtlUnit()
        );
    }


    public boolean clearCache(@NonNull String funcName) {
        val redisKey = getRedisKey(funcName);
        val deleteResult = redisTemplate.delete(redisKey);
        return Boolean.TRUE.equals(deleteResult);
    }

    public String getRedisKey(String funcName) {
        return FUNC_SWITCH_REDIS_KEY_PREFIX + ":" + funcName;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class FuncSwitchCache {
        private String pathExpression;
        private Boolean enabled;
    }

    @org.mapstruct.Mapper(
            unmappedSourcePolicy = ReportingPolicy.ERROR,
            unmappedTargetPolicy = ReportingPolicy.ERROR
    )
    public interface Mapper {
        Mapper INSTANCE = Mappers.getMapper(Mapper.class);

        @BeanMapping(ignoreUnmappedSourceProperties = {"id", "funcName"})
        FuncSwitchCache toCache(FuncSwitch funcSwitch);

        List<FuncSwitchCache> toCacheList(List<FuncSwitch> funcSwitchList);
    }
}


