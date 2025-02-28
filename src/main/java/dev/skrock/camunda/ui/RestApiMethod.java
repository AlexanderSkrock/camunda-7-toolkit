package dev.skrock.camunda.ui;

import org.springframework.http.HttpMethod;

import java.util.List;

public interface RestApiMethod<Result> {

    String name();

    HttpMethod method();

    String path();

    List<? extends RestApiMethodParameter> parameters();

    Result call(Object instance, Object... parameterValues) throws Exception;
}
