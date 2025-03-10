package dev.skrock.camunda.toolkit.rest;

import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SwaggerRestApiMethod implements RestApiMethod {

    private final Method method;

    public SwaggerRestApiMethod(Method method) {
        this.method = method;
    }

    @Override
    public String name() {
        return Optional.ofNullable(method.getAnnotation(Operation.class))
                .map(Operation::operationId)
                .orElseGet(method::getName);
    }

    @Override
    public HttpMethod method() {
        return Optional.ofNullable(method.getAnnotation(RequestMapping.class))
                .map(RequestMapping::method)
                .flatMap(requestMethods -> Arrays.stream(requestMethods).findFirst())
                .map(RequestMethod::asHttpMethod)
                .orElseThrow();
    }

    @Override
    public String path() {
        return Optional.ofNullable(method.getAnnotation(RequestMapping.class))
                .map(RequestMapping::value)
                .flatMap(paths -> Arrays.stream(paths).findFirst())
                .orElseThrow();
    }

    @Override
    public List<? extends RestApiMethodParameter> parameters() {
        return Arrays.stream(method.getParameters())
                .map(SwaggerRestApiMethodParameter::new)
                .toList();
    }

    @Override
    public Class<?> declaringType() {
        return method.getDeclaringClass();
    }

    @Override
    public Object call(Object instance, Object... parameterValues) throws InvocationTargetException, IllegalAccessException {
        if (ArrayUtils.isEmpty(parameterValues)) {
            return method.invoke(instance);
        } else {
            return method.invoke(instance, parameterValues);
        }
    }
}
