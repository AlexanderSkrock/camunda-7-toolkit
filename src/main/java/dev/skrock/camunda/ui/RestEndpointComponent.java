package dev.skrock.camunda.ui;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.NativeTableHeader;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class RestEndpointComponent extends VerticalLayout {

    private final Method method;

    private final Object instance;

    public RestEndpointComponent(Method method, Object instance) {
        if (!method.isAnnotationPresent(RequestMapping.class)) {
            throw new IllegalArgumentException("no request mapping found");
        }
        if (!method.getDeclaringClass().isInstance(instance)) {
            throw new IllegalArgumentException("incompatible instance");
        }
        this.method = method;
        this.instance = instance;

        String name = Optional.ofNullable(method.getAnnotation(Operation.class))
                .map(Operation::summary)
                .orElse(method.getName());
        add(new H1(name));

        Optional.ofNullable(method.getAnnotation(RequestMapping.class)).ifPresent(requestMapping -> {
            // TODO support multiple methods
            RequestMethod requestMethod = Arrays.stream(requestMapping.method()).findFirst().orElseThrow();
            add(requestMethod.name());
            add(requestMapping.name());
        });

        FormLayout restApiForm = new FormLayout();

        Object[] paramValues = new Object[method.getParameterCount()];
        for (int i = 0; i < method.getParameterCount(); i++) {
            final int index = i;

            Parameter param = method.getParameters()[index];
            restApiForm.addFormItem(
                    ParameterFormField.forParameter(param, evt -> paramValues[index] = evt.getValue()),
                    // TODO fix duplicate labels
                    ParameterFormField.getLabel(param)
            );
        }

        restApiForm.add(new Button("Execute", new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                try {
                    method.invoke(instance, paramValues);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }));

        add(restApiForm);
    }
}
