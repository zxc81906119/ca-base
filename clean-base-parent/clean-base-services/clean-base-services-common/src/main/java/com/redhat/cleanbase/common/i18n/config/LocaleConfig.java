package com.redhat.cleanbase.common.i18n.config;

import com.redhat.cleanbase.common.i18n.config.prop.LocaleProperties;
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

    private static final ThreadLocal<String> INHERITABLE_THREAD_LOCAL = new InheritableThreadLocal<>();
    private static final ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<>();

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName());
        INHERITABLE_THREAD_LOCAL.set("inherit thread local");
        THREAD_LOCAL.set("thread local");
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName());
            System.out.println("sub:" + INHERITABLE_THREAD_LOCAL.get());
            System.out.println("sub:" + THREAD_LOCAL.get());
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName());
                System.out.println("subSub:" + INHERITABLE_THREAD_LOCAL.get());
            }).start();
        }).start();
    }

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
