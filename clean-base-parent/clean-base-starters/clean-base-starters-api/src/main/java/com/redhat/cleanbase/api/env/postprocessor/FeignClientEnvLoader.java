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

import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class FeignClientEnvLoader implements EnvironmentPostProcessor {

    public static final String API_STARTER_APPLICATION_CONFIG = "api-starter-application-config";
    public static final String CONFIG_CLASS_PATH_ROOT_PATH = "config";
    public static final String OPENFEIGN_CONFIG_NAME_FORMAT = "application-openfeign%s.yml";

    private final YamlPropertySourceLoader loader =
            new YamlPropertySourceLoader();

    @Override
    public void postProcessEnvironment(
            ConfigurableEnvironment environment
            , SpringApplication application
    ) {
        val activeProfiles = new ArrayList<>(Arrays.asList(environment.getActiveProfiles()));
        activeProfiles.add("");

        val openfeignConfigPaths = getOpenfeignConfigPaths(activeProfiles);

        val propertySources = environment.getPropertySources();

        for (String configPath : openfeignConfigPaths) {
            try {
                val propertySourceList = loader.load(API_STARTER_APPLICATION_CONFIG, new ClassPathResource(configPath));
                Collections.reverse(propertySourceList);
                propertySourceList.forEach(propertySources::addFirst);
            } catch (Exception e) {
                log.warn("open feign config path: {} not found or read fail", configPath, e);
            }
        }

    }

    private static List<String> getOpenfeignConfigPaths(List<String> activeProfiles) {
        return activeProfiles.stream()
                .map((activeProfile) ->
                        activeProfile.isEmpty()
                                ? activeProfile
                                : "-" + activeProfile
                )
                .distinct()
                .map(OPENFEIGN_CONFIG_NAME_FORMAT::formatted)
                .map((configName) -> CONFIG_CLASS_PATH_ROOT_PATH + "/" + configName)
                .toList();
    }
}
