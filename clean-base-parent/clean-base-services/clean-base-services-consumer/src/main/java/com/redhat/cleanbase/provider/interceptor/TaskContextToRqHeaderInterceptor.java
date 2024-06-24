package com.redhat.cleanbase.provider.interceptor;

import com.redhat.cleanbase.api.interceptor.BeanToRqHeaderInterceptor;
import com.redhat.cleanbase.provider.context.TaskContext;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;

public class TaskContextToRqHeaderInterceptor extends BeanToRqHeaderInterceptor<TaskContext> {

    public TaskContextToRqHeaderInterceptor(ApplicationContext applicationContext) {
        super(applicationContext, TaskContext.class);
    }

    // todo
    @Override
    protected HttpHeaders getHeaders(TaskContext taskContext) {
        return null;
    }
}
