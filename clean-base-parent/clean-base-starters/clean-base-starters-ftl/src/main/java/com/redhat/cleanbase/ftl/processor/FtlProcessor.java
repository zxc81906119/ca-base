package com.redhat.cleanbase.ftl.processor;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

@RequiredArgsConstructor
public class FtlProcessor {

    private final Configuration configuration;

    public String generateFtlString(String templatePath, Map<String, Object> dataModel) throws TemplateException, IOException {
        val stringWriter = new StringWriter();
        generateFtl(templatePath, dataModel, stringWriter);
        return stringWriter.toString();
    }

    public void generateFtl(String templatePath, Map<String, Object> dataModel, Writer writer) throws TemplateException, IOException {
        try (writer) {
            val template = configuration.getTemplate(templatePath);
            template.process(dataModel, writer);
        }
    }
}
