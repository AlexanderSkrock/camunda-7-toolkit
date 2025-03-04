package dev.skrock.camunda.toolkit.ui.rest;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import dev.skrock.camunda.toolkit.rest.RestApiMethod;
import dev.skrock.camunda.toolkit.rest.RestApiMethodParameter;

import java.util.List;

public class RestApiMethodComponent extends VerticalLayout {

    private RestApiMethod apiMethod;
    private Object[] paramValues = new Object[0];

    public RestApiMethod getApiMethod() {
        return apiMethod;
    }

    public Object[] getParamValues() {
        return paramValues;
    }

    public void setApiMethod(RestApiMethod apiMethod) {
        this.apiMethod = apiMethod;
        if (apiMethod != null) {
            paramValues = new Object[apiMethod.parameters().size()];
        } else {
            paramValues = new Object[0];
        }
        refreshComponent();
    }

    protected void refreshComponent() {
        removeAll();
        if (apiMethod != null) {
            add(new H1(apiMethod.name()));
            add(apiMethod.method().name());
            add(apiMethod.path());

            add(createForm());
        }
    }

    protected FormLayout createForm() {
        FormLayout restApiForm = new FormLayout();

        List<? extends RestApiMethodParameter> apiParameters = apiMethod.parameters();
        apiParameters.forEach(apiParam -> {
            final int index = apiParameters.indexOf(apiParam);
            restApiForm.addFormItem(
                    ParameterFormField.forParameter(apiParam, evt -> paramValues[index] = evt.getValue()),
                    apiParam.name()
            );
        });

        return restApiForm;
    }
}
