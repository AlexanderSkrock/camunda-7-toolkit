package dev.skrock.camunda.toolkit.ui.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import de.f0rce.ace.AceEditor;
import de.f0rce.ace.enums.AceMode;
import dev.skrock.camunda.toolkit.api.CamundaRestApiProvider;
import dev.skrock.camunda.toolkit.rest.RestApiMethod;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;

@Route("/rest")
public class RestApiView extends VerticalLayout {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    public RestApiView(CamundaRestApiProvider apiProvider) {
        RestApiMethodComponent apiMethodComponent = new RestApiMethodComponent();

        ComboBox<RestApiMethod> comboBox = new ComboBox<>(new HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<ComboBox<RestApiMethod>, RestApiMethod>>() {
            @Override
            public void valueChanged(AbstractField.ComponentValueChangeEvent<ComboBox<RestApiMethod>, RestApiMethod> event) {
                RestApiMethod method = event.getValue();
                apiMethodComponent.setApiMethod(method);
            }
        });
        comboBox.setItemLabelGenerator(RestApiMethod::name);

        List<RestApiMethod> apiMethods = apiProvider.getApiMethods().values().stream().flatMap(Collection::stream).toList();
        comboBox.setItems(apiMethods);

        AceEditor resultPane = new AceEditor();
        resultPane.setMode(AceMode.json);

        Button executeButton = new Button("Execute", new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                try {
                    RestApiMethod apiMethod = apiMethodComponent.getApiMethod();
                    Object[] parameterValues = apiMethodComponent.getParamValues();
                    Object instance = apiProvider.getApiImplementation(apiMethod);

                    Object result = apiMethod.call(instance, parameterValues);
                    String json = MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(result);
                    resultPane.setValue(json);
                } catch (Exception e) {
                    // TODO handle error
                    resultPane.setValue("");
                    throw new RuntimeException(e);
                }
            }
        });

        // TODO add option to batch requests

        add(comboBox, apiMethodComponent, executeButton, resultPane);
    }
}
