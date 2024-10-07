package com.redhat.cleanbase.web.servlet.exception.handler;

import com.redhat.cleanbase.common.utils.CastUtils;
import com.redhat.cleanbase.exception.handler.ExceptionHandler;
import com.redhat.cleanbase.web.servlet.exception.condition.ExceptionCondition;
import com.redhat.cleanbase.web.servlet.response.processor.WriteRsEntityToRsProcessor;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import java.util.*;

public abstract class AbstractDelegateExceptionHandler<D> implements ExceptionHandler<D, Exception> {

    private final WriteRsEntityToRsProcessor writeRsEntityToRs;

    private final Map<Class<? extends Exception>, List<ExceptionCondition<D, ?>>> exceptionHandlerMap = new HashMap<>();

    public AbstractDelegateExceptionHandler(List<ExceptionCondition<D, ?>> exceptionHandlers, WriteRsEntityToRsProcessor writeRsEntityToRs) {
        this.writeRsEntityToRs = writeRsEntityToRs;
        exceptionHandlers.forEach((exceptionHandler) ->
                exceptionHandlerMap.computeIfAbsent(exceptionHandler.getIdentifier(),
                                (key) -> new ArrayList<>()
                        )
                        .add(exceptionHandler)
        );
    }

    private Optional<? extends ExceptionCondition<D, ?>> getExceptionHandler(D d, Exception e) {
        return exceptionHandlerMap.entrySet().stream()
                .filter((exceptionHandlerEntry) ->
                        exceptionHandlerEntry.getKey().isInstance(e)
                )
                // 對 候選的 entry 做排序,最靠近處理例外的例外處理者優先
                .sorted(
                        Map.Entry.comparingByKey(
                                (e1, e2) -> {
                                    if (e1.equals(e2)) {
                                        return 0;
                                    }
                                    return e1.isAssignableFrom(e2) ? 1 : -1;
                                }
                        )
                )
                // 每個 entry 轉成例外處理者
                .map(Map.Entry::getValue)
                // 攤平成所有例外處理者一份
                .flatMap(Collection::stream)
                .filter((exceptionHandler) ->
                        exceptionHandler.isSupported(d, CastUtils.cast(e))
                )
                .findFirst();
    }

    public void handleAndWriteRs(D d, HttpServletResponse response, Exception e) {
        writeRsEntityToRs.lazyWriteRsWithRsEntitySupplier(response)
                .accept(() -> handle(d, e));
    }

    @Override
    public final ResponseEntity<?> handle(D d, Exception e) {
        return getExceptionHandler(d, e)
                .map((condition) ->
                        condition.handle(d, CastUtils.cast(e))
                )
                .orElse(null);
    }
}
