package com.redhat.cleanbase.pdf.config;

import com.redhat.cleanbase.pdf.processor.BasePdfProcessor;
import com.redhat.cleanbase.pdf.processor.PdfProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PdfAutoConfig {

    @ConditionalOnMissingBean(BasePdfProcessor.class)
    @Bean
    public PdfProcessor pdfProcessor() {
        return new PdfProcessor();
    }

}
