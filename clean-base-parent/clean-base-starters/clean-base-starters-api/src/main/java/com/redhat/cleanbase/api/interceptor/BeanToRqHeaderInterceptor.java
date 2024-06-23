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
public abstract class BeanToRqHeaderInterceptor<T> implements RequestInterceptor {

    private final ApplicationContext applicationContext;
    private final Class<T> beanType;

    @Override
    public void apply(RequestTemplate template) {
        try {
            val taskContext =
                    applicationContext.getBean(beanType);

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

    abstract protected HttpHeaders getHeaders(T t);

    protected boolean isThrow(RuntimeException e) {
        return false;
    }


    public static class Default extends BeanToRqHeaderInterceptor<TaskContext> {

        public Default(ApplicationContext applicationContext) {
            super(applicationContext, TaskContext.class);
        }

        @Override
        protected HttpHeaders getHeaders(TaskContext taskContext) {
            return null;
        }
    }
}
