package com.redhat.cleanbase.common.i18n.locale.config;

import com.redhat.cleanbase.common.i18n.locale.config.prop.LocaleProperties;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@EnableConfigurationProperties(LocaleProperties.class)
@Configuration
@RequiredArgsConstructor
public class LocaleConfig implements WebMvcConfigurer {
    private final LocaleProperties localeProperties;

    @Bean(DispatcherServlet.LOCALE_RESOLVER_BEAN_NAME)
    public LocaleResolver localeResolver() {
        val cookieLocaleResolver = new CookieLocaleResolver();
        cookieLocaleResolver.setDefaultLocale(localeProperties.getDefaultLocale());
        return cookieLocaleResolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        val localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName(localeProperties.getRequestChangeLocaleParam());
        return localeChangeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
