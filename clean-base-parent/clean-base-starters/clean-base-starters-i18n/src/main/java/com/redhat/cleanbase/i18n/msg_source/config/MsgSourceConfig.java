package com.redhat.cleanbase.i18n.msg_source.config;

import com.redhat.cleanbase.i18n.msg_source.CompositeMsgSource;
import com.redhat.cleanbase.i18n.msg_source.PropMsgSourceProxy;
import com.redhat.cleanbase.i18n.msg_source.base.CustomMsgSource;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.context.MessageSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.AbstractResourceBasedMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.List;

@AutoConfigureBefore(MessageSourceAutoConfiguration.class)
@Configuration
public class MsgSourceConfig {

    public static final String DEFAULT_CUSTOM_MSG_SOURCE_BEAN_NAME = "defaultCustomMsgSource";

    @ConditionalOnMissingBean(MessageSourceProperties.class)
    @Bean
    @ConfigurationProperties(prefix = "spring.messages")
    public MessageSourceProperties messageSourceProperties() {
        return new MessageSourceProperties();
    }

    @ConditionalOnMissingBean(AbstractResourceBasedMessageSource.class)
    @Bean
    public AbstractResourceBasedMessageSource propMsgSource(MessageSourceProperties properties) {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        if (StringUtils.hasText(properties.getBasename())) {
            messageSource.setBasenames(StringUtils
                    .commaDelimitedListToStringArray(StringUtils.trimAllWhitespace(properties.getBasename())));
        }
        if (properties.getEncoding() != null) {
            messageSource.setDefaultEncoding(properties.getEncoding().name());
        }
        messageSource.setFallbackToSystemLocale(properties.isFallbackToSystemLocale());
        Duration cacheDuration = properties.getCacheDuration();
        if (cacheDuration != null) {
            messageSource.setCacheMillis(cacheDuration.toMillis());
        }
        messageSource.setAlwaysUseMessageFormat(properties.isAlwaysUseMessageFormat());
        messageSource.setUseCodeAsDefaultMessage(properties.isUseCodeAsDefaultMessage());
        return messageSource;
    }

    @ConditionalOnMissingBean(name = DEFAULT_CUSTOM_MSG_SOURCE_BEAN_NAME)
    @Bean(DEFAULT_CUSTOM_MSG_SOURCE_BEAN_NAME)
    public CustomMsgSource defaultCustomMsgSource(AbstractResourceBasedMessageSource delegate) {
        return new PropMsgSourceProxy(delegate);
    }

    @ConditionalOnMissingBean(name = AbstractApplicationContext.MESSAGE_SOURCE_BEAN_NAME)
    @Bean(AbstractApplicationContext.MESSAGE_SOURCE_BEAN_NAME)
    public CompositeMsgSource compositeMsgSource(List<CustomMsgSource> customMsgSources) {
        return new CompositeMsgSource(customMsgSources);
    }
}
