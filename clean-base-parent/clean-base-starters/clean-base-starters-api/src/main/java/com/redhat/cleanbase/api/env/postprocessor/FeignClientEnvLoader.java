package com.redhat.cleanbase.api.env.postprocessor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class FeignClientEnvLoader implements EnvironmentPostProcessor {

    private final YamlPropertySourceLoader loader =
            new YamlPropertySourceLoader();

    @Override
    public void postProcessEnvironment(
            ConfigurableEnvironment environment
            , SpringApplication application
    ) {
        try {
            val resource = new ClassPathResource("config/application-openfeign.yml");
            val sourceList = loader.load("openfeign-starter-config", resource);
            val propertySources = environment.getPropertySources();
            sourceList.forEach(propertySources::addLast);
        } catch (IOException e) {
            log.error("custom openfeign properties load failed !!!", e);
        }
    }
}
