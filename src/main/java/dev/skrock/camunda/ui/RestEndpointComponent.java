package dev.skrock.camunda.ui;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class RestEndpointComponent extends VerticalLayout {

    public RestEndpointComponent(Method method, Object instance) {
        if (!method.isAnnotationPresent(RequestMapping.class)) {
            throw new IllegalArgumentException("no request mapping found");
        }
        if (!method.getDeclaringClass().isInstance(instance)) {
            throw new IllegalArgumentException("incompatible instance");
        }
        RestApiMethod<?> apiMethod = new SwaggerRestApiMethod(method);

        add(new H1(apiMethod.name()));

        add(apiMethod.method().name());
        add(apiMethod.path());

        FormLayout restApiForm = new FormLayout();

        List<? extends RestApiMethodParameter> apiParameters = apiMethod.parameters();
        Object[] paramValues = new Object[apiParameters.size()];
        apiParameters.forEach(apiParam -> {
            final int index = apiParameters.indexOf(apiParam);
            restApiForm.addFormItem(
                    ParameterFormField.forParameter(apiParam, evt -> paramValues[index] = evt.getValue()),
                    apiParam.name()
            );
        });

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
