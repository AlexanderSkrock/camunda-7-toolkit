package dev.skrock.camunda.toolkit.ui.rest;

import org.springframework.http.HttpMethod;

import java.util.List;

public interface RestApiMethod {

    String name();

    HttpMethod method();

    String path();

    List<? extends RestApiMethodParameter> parameters();

    Object call(Object instance, Object... parameterValues) throws Exception;
}
