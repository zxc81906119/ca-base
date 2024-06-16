package com.redhat.cleanbasestarterspdf.config;

import com.redhat.cleanbasestarterspdf.processor.PdfProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PdfConfig {
    @Bean
    public PdfProcessor pdfProcessor() {
        return new PdfProcessor();
    }
}
