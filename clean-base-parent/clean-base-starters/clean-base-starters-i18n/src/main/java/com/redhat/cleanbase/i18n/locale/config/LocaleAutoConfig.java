package com.redhat.cleanbase.i18n.locale.config;

import com.redhat.cleanbase.i18n.locale.config.prop.LocaleProperties;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@RequiredArgsConstructor
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableConfigurationProperties(LocaleProperties.class)
public class LocaleAutoConfig {

    private final LocaleProperties localeProperties;

    @ConditionalOnMissingBean
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        val localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName(localeProperties.getRequestChangeLocaleParam());
        return localeChangeInterceptor;
    }

    @RequiredArgsConstructor
    @Configuration
    static class MvcConfig implements WebMvcConfigurer {
        private final LocaleChangeInterceptor localeChangeInterceptor;

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(localeChangeInterceptor);
        }
    }


}
