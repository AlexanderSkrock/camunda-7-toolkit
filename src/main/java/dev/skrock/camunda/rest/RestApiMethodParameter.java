package dev.skrock.camunda.rest;

public interface RestApiMethodParameter {

    String name();

    Class<?> type();

    boolean required();
}
