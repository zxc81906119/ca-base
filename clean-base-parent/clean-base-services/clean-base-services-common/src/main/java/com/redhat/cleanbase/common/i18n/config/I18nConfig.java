package com.redhat.cleanbase.common.i18n.config;

import com.redhat.cleanbase.common.i18n.msgsource.CustomDBMsgSource;
import com.redhat.cleanbase.common.i18n.msgsource.CustomDBMsgSource1;
import com.redhat.cleanbase.common.i18n.msgsource.CompositeMessageSource;
import com.redhat.cleanbase.common.i18n.msgsource.CustomPropertiesMessageSource;
import lombok.val;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.context.MessageSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.StringUtils;

import java.util.List;

@ConditionalOnClass(AbstractMessageSource.class)
@Configuration
public class I18nConfig {

    public static final String CUSTOM_CONFIG_MESSAGE_SOURCE_BEAN_NAME = "customConfigMessageSource";
    public static final String CUSTOM_DB_MESSAGE_SOURCE_BEAN_NAME = "customDBMessageSource";
    public static final String CUSTOM_DB_MESSAGE_SOURCE_1_BEAN_NAME = "customDBMessageSource1";

    @Bean
    @ConfigurationProperties(prefix = "spring.messages")
    public MessageSourceProperties messageSourceProperties() {
        return new MessageSourceProperties();
    }

    @Bean(CUSTOM_CONFIG_MESSAGE_SOURCE_BEAN_NAME)
    public MessageSource messageSource() {
        val properties = messageSourceProperties();
        val messageSource = new ResourceBundleMessageSource();
        if (StringUtils.hasText(properties.getBasename())) {
            messageSource.setBasenames(StringUtils
                    .commaDelimitedListToStringArray(StringUtils.trimAllWhitespace(properties.getBasename())));
        }
        if (properties.getEncoding() != null) {
            messageSource.setDefaultEncoding(properties.getEncoding().name());
        }
        messageSource.setFallbackToSystemLocale(properties.isFallbackToSystemLocale());
        val cacheDuration = properties.getCacheDuration();
        if (cacheDuration != null) {
            messageSource.setCacheMillis(cacheDuration.toMillis());
        }
        messageSource.setAlwaysUseMessageFormat(properties.isAlwaysUseMessageFormat());
        messageSource.setUseCodeAsDefaultMessage(properties.isUseCodeAsDefaultMessage());
        return new CustomPropertiesMessageSource(messageSource);
    }

    @Bean(CUSTOM_DB_MESSAGE_SOURCE_BEAN_NAME)
    public MessageSource customDBMessageSource() {
        return new CustomDBMsgSource();
    }

    @Bean(CUSTOM_DB_MESSAGE_SOURCE_1_BEAN_NAME)
    public MessageSource customDBMessageSource1() {
        return new CustomDBMsgSource1();
    }

    @Bean(AbstractApplicationContext.MESSAGE_SOURCE_BEAN_NAME)
    public MessageSource compositeMessageSource() {
        return new CompositeMessageSource(
                List.of(
                        customDBMessageSource(),
                        customDBMessageSource1(),
                        messageSource()
                )
        );
    }
}
