package com.redhat.cleanbase.pdf.config;

import com.redhat.cleanbase.pdf.processor.PdfProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PdfConfig {
    @Bean
    public PdfProcessor pdfProcessor() {
        return new PdfProcessor();
    }
}
