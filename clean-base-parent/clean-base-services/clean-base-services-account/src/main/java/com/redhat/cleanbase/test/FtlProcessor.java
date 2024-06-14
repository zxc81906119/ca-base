package com.redhat.cleanbase.test;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

@RequiredArgsConstructor
public class FtlProcessor {

    private final Configuration configuration;

    public void generateFtl(String templatePath, Map<String, Object> dataModel, Writer writer) throws TemplateException, IOException {
        val template = configuration.getTemplate(templatePath);
        template.process(dataModel, writer);
    }
}
