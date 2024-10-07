package com.redhat.cleanbase.convert.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public abstract class JacksonJsonParser<SELF extends JacksonJsonParser<SELF>> extends JsonParser<ObjectMapper, SELF> {

    public JacksonJsonParser(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public <T> T parse(String json, Class<T> clazz) throws JsonProcessingException {
        return delegate.readValue(json, clazz);
    }

    @Override
    public String toJson(Object o) throws JsonProcessingException {
        return delegate.writeValueAsString(o);
    }

    public <T> T parse(String json, TypeReference<T> typeReference) throws JsonProcessingException {
        return delegate.readValue(json, typeReference);
    }

    public <T> T parse(InputStream inputStream, TypeReference<T> typeReference) throws IOException {
        return delegate.readValue(inputStream, typeReference);
    }

    @Override
    protected ObjectMapper copyDelegate(ObjectMapper originDelegate) {
        return originDelegate.copy();
    }

}
