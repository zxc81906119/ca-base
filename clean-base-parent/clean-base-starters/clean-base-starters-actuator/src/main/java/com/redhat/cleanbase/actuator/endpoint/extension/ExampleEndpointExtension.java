package com.redhat.cleanbase.actuator.endpoint.extension;

import com.redhat.cleanbase.actuator.endpoint.ExampleEndpoint;
import com.redhat.cleanbase.actuator.model.CustomData;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.web.WebEndpointResponse;
import org.springframework.boot.actuate.endpoint.web.annotation.EndpointWebExtension;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EndpointWebExtension(endpoint = ExampleEndpoint.class)
public class ExampleEndpointExtension {
    private final ExampleEndpoint endpoint;

    @ReadOperation
    public WebEndpointResponse<CustomData> getData() {
        val data = endpoint.data.getData();
        val clone = Converter.INSTANCE.clone(data);
        clone.setDataInt(2);
        return new WebEndpointResponse<>(clone);
    }

    @Mapper(
            unmappedSourcePolicy = ReportingPolicy.ERROR,
            unmappedTargetPolicy = ReportingPolicy.ERROR
    )
    public interface Converter {
        Converter INSTANCE = Mappers.getMapper(Converter.class);

        CustomData clone(CustomData customData);
    }
}
