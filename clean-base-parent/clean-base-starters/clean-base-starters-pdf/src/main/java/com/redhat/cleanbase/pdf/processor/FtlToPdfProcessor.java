package com.redhat.cleanbase.pdf.processor;

import com.redhat.cleanbase.ftl.processor.FtlProcessor;
import freemarker.template.TemplateException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
public class FtlToPdfProcessor {
    @NonNull
    private final FtlProcessor ftlProcessor;
    @NonNull
    private final PdfProcessor pdfProcessor;

    @NonNull
    private final String template;
    private final Map<String, Object> map;

    private String html;


    public String generateHtml() throws TemplateException, IOException {
        if (html != null) {
            return html;
        }
        return html = ftlProcessor.generateFtlString(template, map);
    }

    public void generatePdf(File pdfFile) throws TemplateException, IOException {
        generatePdf(pdfFile, false);
    }

    public void generatePdf(File pdfFile, boolean replace) throws TemplateException, IOException {
        if (!replace && pdfFile.isFile() && pdfFile.length() != 0) {
            return;
        }
        pdfProcessor.htmlToPdf(pdfFile, generateHtml());
    }

}