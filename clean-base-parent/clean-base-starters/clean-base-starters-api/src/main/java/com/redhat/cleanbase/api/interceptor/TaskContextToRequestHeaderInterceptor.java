package com.redhat.cleanbase.api.interceptor;

import com.redhat.cleanbase.api.context.TaskContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public abstract class TaskContextToRequestHeaderInterceptor implements RequestInterceptor {

    private final ApplicationContext applicationContext;

    @Override
    public void apply(RequestTemplate template) {
        try {
            val taskContext =
                    applicationContext.getBean(TaskContext.class);

            Optional.ofNullable(getHeaders(taskContext))
                    .ifPresent((headers) ->
                            headers.forEach(template::header)
                    );

        } catch (RuntimeException e) {
            log.error("task context to request header occur exception", e);
            if (isThrow(e)) {
                throw e;
            }
        }
    }

    abstract protected HttpHeaders getHeaders(TaskContext taskContext);

    protected boolean isThrow(RuntimeException e) {
        return false;
    }


    public static class Default extends TaskContextToRequestHeaderInterceptor {

        public Default(ApplicationContext applicationContext) {
            super(applicationContext);
        }

        @Override
        protected HttpHeaders getHeaders(TaskContext taskContext) {
            return null;
        }
    }
}
