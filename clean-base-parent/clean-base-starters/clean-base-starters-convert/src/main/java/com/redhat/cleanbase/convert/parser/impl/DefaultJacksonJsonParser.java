package com.redhat.cleanbase.convert.parser.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.cleanbase.convert.parser.JacksonJsonParser;

public class DefaultJacksonJsonParser extends JacksonJsonParser<DefaultJacksonJsonParser> {

    public DefaultJacksonJsonParser(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    protected ObjectMapper createJsonParserDelegate() {
        return new ObjectMapper();
    }

    @Override
    protected DefaultJacksonJsonParser createSelf(ObjectMapper delegate) {
        return new DefaultJacksonJsonParser(delegate);
    }
}
