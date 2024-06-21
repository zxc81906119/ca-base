package com.redhat.cleanbase.interceptor;

import com.redhat.cleanbase.aware.GlobalApplicationContextAware;
import com.redhat.cleanbase.context.TaskContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpHeaders;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public abstract class TaskContextRequestHeaderInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {

        try {
            val taskContext =
                    GlobalApplicationContextAware
                            .getApplicationContext()
                            .getBean(TaskContext.class);

            Optional.ofNullable(getHeaders(taskContext))
                    .ifPresent((headers) ->
                            headers.forEach(template::header)
                    );

        } catch (RuntimeException e) {
            if (isThrow(e)) {
                throw e;
            }
        }
    }

    abstract protected HttpHeaders getHeaders(TaskContext taskContext);

    protected boolean isThrow(RuntimeException e) {
        return false;
    }


}
