package com.redhat.cleanbase.web.servlet.response.processor.impl;

import com.redhat.cleanbase.convert.parser.JsonParser;
import com.redhat.cleanbase.web.servlet.response.processor.RsEntityRsWriter;

public class JsonRsEntityRsWriter extends RsEntityRsWriter {

    public JsonRsEntityRsWriter(JsonParser<?, ?> jsonParser) {
        super(jsonParser::toJson);
    }
}
