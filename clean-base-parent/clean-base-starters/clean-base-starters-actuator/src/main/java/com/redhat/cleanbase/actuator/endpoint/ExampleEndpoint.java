package com.redhat.cleanbase.actuator.endpoint;

import com.redhat.cleanbase.actuator.model.CustomData;
import com.redhat.cleanbase.common.lock.DataWithReadWriteLock;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = "example", enableByDefault = false)
public class ExampleEndpoint {

    public final DataWithReadWriteLock<CustomData> data = new DataWithReadWriteLock<>(new CustomData("init", 0));

    @ReadOperation
    public CustomData getData() {
        return data.getData();
    }

    @WriteOperation
    public void updateData(String name, int newInt) {
        // injects "test" and 42
        data.setData(new CustomData(name, newInt));
    }
}
