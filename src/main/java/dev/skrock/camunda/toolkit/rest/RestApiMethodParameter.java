package dev.skrock.camunda.toolkit.rest;

public interface RestApiMethodParameter {

    String name();

    Class<?> type();

    boolean required();
}
