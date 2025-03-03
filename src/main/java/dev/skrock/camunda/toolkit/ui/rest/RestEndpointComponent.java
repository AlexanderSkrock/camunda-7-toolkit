package dev.skrock.camunda.toolkit.ui.rest;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import dev.skrock.camunda.toolkit.rest.RestApiMethod;
import dev.skrock.camunda.toolkit.rest.RestApiMethodParameter;

import java.util.List;

public class RestEndpointComponent extends VerticalLayout {

    public void unbind() {
        removeAll();
    }

    public void bind(Object instance, RestApiMethod apiMethod) {
        unbind();

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
                    apiMethod.call(instance, paramValues);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }));

        add(restApiForm);
    }
}
