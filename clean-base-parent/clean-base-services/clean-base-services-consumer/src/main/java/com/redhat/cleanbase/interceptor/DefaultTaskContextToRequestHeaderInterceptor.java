package com.redhat.cleanbase.interceptor;

import com.redhat.cleanbase.context.TaskContext;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;

public class DefaultTaskContextToRequestHeaderInterceptor extends TaskContextToRequestHeaderInterceptor {
    public DefaultTaskContextToRequestHeaderInterceptor(ApplicationContext applicationContext) {
        super(applicationContext);
    }

    // todo 因為不知道怎實做
    //  幫我實做罷拖
    @Override
    protected HttpHeaders getHeaders(TaskContext taskContext) {
        return null;
    }
}
