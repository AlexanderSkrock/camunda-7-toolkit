package dev.skrock.camunda.ui;

public interface RestApiMethodParameter {

    String name();

    Class<?> type();

    boolean required();
}
