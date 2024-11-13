package com.redhat.cleanbase.api.interceptor;

import com.redhat.cleanbase.api.data.source.FeignClientDataSource;
import com.redhat.cleanbase.api.context.TaskContext;
import lombok.val;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;

public class TaskContextToRqHeaderInterceptor extends BeanToRqHeaderInterceptor<TaskContext> {


    public TaskContextToRqHeaderInterceptor(ApplicationContext applicationContext) {
        super(applicationContext, TaskContext.class);
    }

    // todo
    @Override
    protected HttpHeaders getHeaders(TaskContext taskContext) {
        val bean = applicationContext.getBean(FeignClientDataSource.class);
        val data = bean.getData();
        return null;
    }
}
