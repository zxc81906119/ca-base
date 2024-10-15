package com.redhat.cleanbase.exception.executor;

import com.redhat.cleanbase.exception.func.CatchFunc;
import com.redhat.cleanbase.exception.func.FinallyFunc;
import com.redhat.cleanbase.exception.func.TryWithResourceFunc;
import com.redhat.cleanbase.exception.result.TryActionResult;
import com.redhat.cleanbase.exception.utils.ExceptionUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.io.Closeable;
import java.util.concurrent.Callable;

@RequiredArgsConstructor
public class ExceptionExecutor<O, R extends Closeable> {

    private final Callable<O> tryFunc;
    private final R resource;
    private final TryWithResourceFunc<R, O> tryWithResourceFunc;
    private final CatchFunc<O> catchFunc;
    private final FinallyFunc finallyFunc;

    public static <OS, RS extends Closeable> ExceptionExecutor<OS, RS> tryWithResourceAction(RS resource, @NonNull TryWithResourceFunc<RS, OS> tryWithResourceFunc) {
        return new ExceptionExecutor<>(null, resource, tryWithResourceFunc, null, null);
    }

    public static <OS> ExceptionExecutor<OS, ?> tryAction(@NonNull Callable<OS> tryFunc) {
        return new ExceptionExecutor<>(tryFunc, null, null, null, null);
    }

    public static <OS, RS extends Closeable> TryActionResult<OS> tryWithResourceNoOtherAction(RS resource, @NonNull TryWithResourceFunc<RS, OS> tryWithResourceFunc) {
        return new ExceptionExecutor<>(null, resource, tryWithResourceFunc, null, null)
                .doAction();
    }

    public ExceptionExecutor<O, R> catchAction(CatchFunc<O> catchFunc) {
        return new ExceptionExecutor<>(tryFunc, resource, tryWithResourceFunc, catchFunc, finallyFunc);
    }

    public TryActionResult<O> catchNoFinallyAction(CatchFunc<O> catchFunc) {
        return new ExceptionExecutor<>(tryFunc, resource, tryWithResourceFunc, catchFunc, finallyFunc)
                .doAction();
    }

    public TryActionResult<O> finallyAction(FinallyFunc finallyFunc) {
        return new ExceptionExecutor<>(tryFunc, resource, tryWithResourceFunc, catchFunc, finallyFunc)
                .doAction();
    }

    protected TryActionResult<O> doAction() {
        final Callable<O> finalTryFunc = () -> {
            if (tryFunc != null) {
                return tryFunc.call();
            }
            if (tryWithResourceFunc != null) {
                // 測試過 resource null , 不會報錯
                try (resource) {
                    return tryWithResourceFunc.doAction(resource);
                }
            }
            throw new UnsupportedOperationException("至少提供 try function 執行");
        };

        val tryActionResult = new TryActionResult<O>();
        try {
            tryActionResult.setData(finalTryFunc.call());
        } catch (Exception execException) {
            if (catchFunc == null) {
                tryActionResult.setException(execException);
            } else {
                try {
                    tryActionResult.setData(catchFunc.doAction(execException));
                } catch (Exception catchException) {
                    setExceptionAndProcess(tryActionResult, execException, catchException);
                }
            }
        } finally {
            if (finallyFunc != null) {
                try {
                    finallyFunc.doAction();
                } catch (Exception finallyException) {
                    val postCatchException = tryActionResult.getException();
                    setExceptionAndProcess(tryActionResult, postCatchException, finallyException);
                }
            }
        }
        return tryActionResult;
    }

    public void setExceptionAndProcess(@NonNull TryActionResult<O> tryActionResult, Exception oldException, @NonNull Exception newException) {
        if (oldException != null && oldException != newException) {
            ExceptionUtils.setCauseOrSuppressed(newException, oldException);
        }
        tryActionResult.setException(newException);
    }
}