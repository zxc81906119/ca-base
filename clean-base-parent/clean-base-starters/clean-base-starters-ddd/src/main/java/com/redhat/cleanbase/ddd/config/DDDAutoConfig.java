package com.redhat.cleanbase.ddd.config;


import com.redhat.cleanbase.ddd.service.DomainService;
import com.redhat.cleanbase.ddd.usecase.BaseUseCase;
import com.redhat.cleanbase.common.constants.GenericConstants;
import com.redhat.cleanbase.ddd.port.Port;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

// 缺點是 base packages 沒有表達式
@ComponentScan(
        useDefaultFilters = false,
        basePackages = GenericConstants.BASE_PACKAGE_NAME,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = {
                                BaseUseCase.class,
                                DomainService.class,
                                Port.class
                        }
                ),
        }
)
@Configuration
public class DDDAutoConfig {
}
