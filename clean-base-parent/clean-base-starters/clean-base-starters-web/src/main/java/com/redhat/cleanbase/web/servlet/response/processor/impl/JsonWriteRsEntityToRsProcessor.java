package com.redhat.cleanbase.web.servlet.response.processor.impl;

import com.redhat.cleanbase.convert.parser.JsonParser;
import com.redhat.cleanbase.web.servlet.response.processor.WriteRsEntityToRsProcessor;

public class JsonWriteRsEntityToRsProcessor extends WriteRsEntityToRsProcessor {

    public JsonWriteRsEntityToRsProcessor(JsonParser<?, ?> jsonParser) {
        super(jsonParser::toJson);
    }
}
