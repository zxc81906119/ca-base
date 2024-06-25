package com.redhat.cleanbase.api.annotation;

import com.redhat.cleanbase.api.registrar.PowerFeignClientsRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(PowerFeignClientsRegistrar.class)
public @interface EnablePowerFeignClients {

    String[] value() default {};

    String[] basePackages() default {};

    Class<?>[] basePackageClasses() default {};

    Class<?>[] defaultConfiguration() default {};

    Class<?>[] clients() default {};
}
