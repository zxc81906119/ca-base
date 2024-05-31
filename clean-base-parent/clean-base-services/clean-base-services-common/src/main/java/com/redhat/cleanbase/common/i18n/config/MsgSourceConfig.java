package com.redhat.cleanbase.common.i18n.config;

import com.redhat.cleanbase.common.i18n.msg_source.CustomDBMsgSource;
import com.redhat.cleanbase.common.i18n.msg_source.CustomDBMsgSource1;
import com.redhat.cleanbase.common.i18n.msg_source.CompositeMsgSource;
import com.redhat.cleanbase.common.i18n.msg_source.CustomPropMsgSource;
import lombok.val;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.context.MessageSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.StringUtils;

import java.util.List;

@ConditionalOnClass(AbstractMessageSource.class)
@Configuration
public class MsgSourceConfig {

    public static final String CUSTOM_CONFIG_MESSAGE_SOURCE_BEAN_NAME = "customConfigMessageSource";
    public static final String CUSTOM_DB_MESSAGE_SOURCE_BEAN_NAME = "customDBMessageSource";
    public static final String CUSTOM_DB_MESSAGE_SOURCE_1_BEAN_NAME = "customDBMessageSource1";

    @Bean
    @ConfigurationProperties(prefix = "spring.messages")
    public MessageSourceProperties messageSourceProperties() {
        return new MessageSourceProperties();
    }

    @Bean(CUSTOM_CONFIG_MESSAGE_SOURCE_BEAN_NAME)
    public CustomPropMsgSource customPropMsgSource() {
        val sourceProperties = messageSourceProperties();
        val resourceBundleMessageSource = new ResourceBundleMessageSource();
        if (StringUtils.hasText(sourceProperties.getBasename())) {
            resourceBundleMessageSource.setBasenames(StringUtils
                    .commaDelimitedListToStringArray(StringUtils.trimAllWhitespace(sourceProperties.getBasename())));
        }
        if (sourceProperties.getEncoding() != null) {
            resourceBundleMessageSource.setDefaultEncoding(sourceProperties.getEncoding().name());
        }
        resourceBundleMessageSource.setFallbackToSystemLocale(sourceProperties.isFallbackToSystemLocale());
        val cacheDuration = sourceProperties.getCacheDuration();
        if (cacheDuration != null) {
            resourceBundleMessageSource.setCacheMillis(cacheDuration.toMillis());
        }
        resourceBundleMessageSource.setAlwaysUseMessageFormat(sourceProperties.isAlwaysUseMessageFormat());
        resourceBundleMessageSource.setUseCodeAsDefaultMessage(sourceProperties.isUseCodeAsDefaultMessage());
        return new CustomPropMsgSource(resourceBundleMessageSource);
    }

    @Bean(CUSTOM_DB_MESSAGE_SOURCE_BEAN_NAME)
    public CustomDBMsgSource customDBMsgSource() {
        return new CustomDBMsgSource();
    }

    @Bean(CUSTOM_DB_MESSAGE_SOURCE_1_BEAN_NAME)
    public CustomDBMsgSource1 customDBMsgSource1() {
        return new CustomDBMsgSource1();
    }

    @Bean(AbstractApplicationContext.MESSAGE_SOURCE_BEAN_NAME)
    public CompositeMsgSource compositeMsgSource() {
        return new CompositeMsgSource(
                List.of(
                        customDBMsgSource(),
                        customDBMsgSource1(),
                        customPropMsgSource()
                )
        );
    }
}
