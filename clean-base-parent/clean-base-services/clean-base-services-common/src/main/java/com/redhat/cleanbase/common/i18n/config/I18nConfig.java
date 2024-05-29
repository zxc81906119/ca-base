package com.redhat.cleanbase.common.i18n.config;

import com.redhat.cleanbase.common.i18n.msgsource.CustomDBMsgSource;
import com.redhat.cleanbase.common.i18n.msgsource.CustomDBMsgSource1;
import com.redhat.cleanbase.common.i18n.msgsource.CompositeMessageSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.AbstractMessageSource;

import java.util.List;

@ConditionalOnClass(AbstractMessageSource.class)
@Configuration
public class I18nConfig {

    public static final String CUSTOM_DB_MESSAGE_SOURCE_BEAN_NAME = "customDBMessageSource";
    public static final String CUSTOM_DB_MESSAGE_SOURCE_1_BEAN_NAME = "customDBMessageSource1";

    @Bean(CUSTOM_DB_MESSAGE_SOURCE_BEAN_NAME)
    public CustomDBMsgSource customDBMessageSource() {
        return new CustomDBMsgSource();
    }

    @Bean(CUSTOM_DB_MESSAGE_SOURCE_1_BEAN_NAME)
    public CustomDBMsgSource1 customDBMessageSource1() {
        return new CustomDBMsgSource1();
    }

    @Bean
    public CompositeMessageSource compositeMessageSource(@Qualifier(AbstractApplicationContext.MESSAGE_SOURCE_BEAN_NAME) MessageSource messageSource) {
        // 順序這邊自己調整
        return new CompositeMessageSource(
                List.of(
                        customDBMessageSource(),
                        customDBMessageSource1(),
                        messageSource
                )
        );
    }
}
