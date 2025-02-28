package dev.skrock.camunda.ui;

import lombok.AllArgsConstructor;

import java.lang.reflect.Parameter;
import java.util.Optional;

@AllArgsConstructor
public class SwaggerRestApiMethodParameter implements RestApiMethodParameter {

    private final Parameter parameter;

    @Override
    public String name() {
        return Optional.ofNullable(parameter.getAnnotation(io.swagger.v3.oas.annotations.Parameter.class))
                .map(io.swagger.v3.oas.annotations.Parameter::name)
                .orElse(parameter.getName());
    }

    @Override
    public Class<?> type() {
        return parameter.getType();
    }

    @Override
    public boolean required() {
        return Optional.ofNullable(parameter.getAnnotation(io.swagger.v3.oas.annotations.Parameter.class))
                .map(io.swagger.v3.oas.annotations.Parameter::required)
                .orElse(false);
    }
}
