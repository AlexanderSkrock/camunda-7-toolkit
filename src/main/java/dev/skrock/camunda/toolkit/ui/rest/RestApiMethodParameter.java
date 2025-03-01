package dev.skrock.camunda.toolkit.ui.rest;

public interface RestApiMethodParameter {

    String name();

    Class<?> type();

    boolean required();
}
