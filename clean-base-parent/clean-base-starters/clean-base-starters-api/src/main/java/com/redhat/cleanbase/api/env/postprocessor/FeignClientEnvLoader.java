package com.redhat.cleanbase.api.env.postprocessor;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.boot.logging.DeferredLog;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class FeignClientEnvLoader implements EnvironmentPostProcessor {

    public static final String API_STARTER_CONFIG = "api-starter-config";
    public static final String PROPERTY_SOURCE_NAME_FORMAT = "[" + API_STARTER_CONFIG + "] -> [%s]";
    public static final String CONFIG_CLASS_PATH_ROOT_PATH = "config";
    public static final String OPENFEIGN_CONFIG_NAME_FORMAT = "application-openfeign%s.yml";

    private final DeferredLog deferredLog = new DeferredLog();

    private final YamlPropertySourceLoader loader = new YamlPropertySourceLoader();

    @Override
    public void postProcessEnvironment(
            ConfigurableEnvironment environment
            , SpringApplication application
    ) {
        application.addInitializers(ctx -> deferredLog.replayTo(FeignClientEnvLoader.class));

        val activeProfiles = new ArrayList<>(Arrays.asList(environment.getActiveProfiles()));
        activeProfiles.add("");

        val openfeignConfigPaths = getOpenfeignConfigPaths(activeProfiles);

        val propertySources = environment.getPropertySources();

        for (String configPath : openfeignConfigPaths) {
            try {
                val propertySourceName = PROPERTY_SOURCE_NAME_FORMAT.formatted(configPath);
                val propertySourceList = loader.load(propertySourceName, new ClassPathResource(configPath));
                Collections.reverse(propertySourceList);
                propertySourceList.forEach(propertySources::addFirst);
            } catch (Exception e) {
                deferredLog.warn("config path: %s , load failed !".formatted(configPath));
            }
        }

    }

    private static List<String> getOpenfeignConfigPaths(List<String> activeProfiles) {
        return activeProfiles.stream()
                .distinct()
                .map((activeProfile) ->
                        activeProfile.isEmpty()
                                ? activeProfile
                                : "-" + activeProfile
                )
                .map(OPENFEIGN_CONFIG_NAME_FORMAT::formatted)
                .map((configName) -> CONFIG_CLASS_PATH_ROOT_PATH + "/" + configName)
                .toList();
    }

}
