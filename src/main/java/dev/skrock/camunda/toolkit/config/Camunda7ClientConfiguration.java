package dev.skrock.camunda.toolkit.config;

import dev.skrock.camunda.toolkit.engine.ConfigurableCamundaEngineProvider;
import dev.skrock.camunda.toolkit.engine.RemoteCamundaEngine;
import feign.RequestInterceptor;
import feign.auth.BasicAuthRequestInterceptor;
import org.camunda.community.rest.client.EnableCamundaFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.Objects;

@Configuration
@EnableCamundaFeignClients
public class Camunda7ClientConfiguration {

    public static final Package CAMUNDA_REST_CLIENTS_PACKAGE = Camunda7ClientConfiguration.class.getClassLoader().getDefinedPackage("org.camunda.community.rest.client.api");

    @Bean
    RequestInterceptor dynamicTargetInterceptor(ConfigurableCamundaEngineProvider engineProvider) {
        return requestTemplate -> {
            Class<?> sourceClass = requestTemplate.feignTarget().type();
            if (Objects.equals(sourceClass.getPackage().getName(), CAMUNDA_REST_CLIENTS_PACKAGE.getName())) {
                RemoteCamundaEngine engine = engineProvider.provide();
                requestTemplate.target(engine.getUrl());
            }
        };
    }

    @Bean
    RequestInterceptor dynamicAuthInterceptor(ConfigurableCamundaEngineProvider engineProvider) {
        return requestTemplate -> {
            Class<?> sourceClass = requestTemplate.feignTarget().type();
            if (Objects.equals(sourceClass.getPackage().getName(), CAMUNDA_REST_CLIENTS_PACKAGE.getName())) {
                RemoteCamundaEngine engine = engineProvider.provide();
                new BasicAuthRequestInterceptor(engine.getUsername(), engine.getPassword()).apply(requestTemplate);
            }
        };
    }

    @Bean
    ConfigurableCamundaEngineProvider engineProvider(ToolkitProperties toolkitProperties) {
        if (CollectionUtils.isEmpty(toolkitProperties.getEngines())) {
            throw new IllegalStateException("no engines configured");
        }
        ConfigurableCamundaEngineProvider provider = new ConfigurableCamundaEngineProvider();
        provider.configureEngine(toolkitProperties.getEngines().get(0));
        return provider;
    }
}
