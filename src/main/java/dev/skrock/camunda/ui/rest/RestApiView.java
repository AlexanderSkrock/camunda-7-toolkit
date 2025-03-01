package dev.skrock.camunda.ui.rest;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import dev.skrock.camunda.rest.RestApiMethod;
import dev.skrock.camunda.rest.SwaggerRestApiMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.camunda.community.rest.client.api.HistoryApi;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.Collection;

@Route("/rest")
public class RestApiView extends VerticalLayout {

    private final RestEndpointComponent restEndpointComponent = new RestEndpointComponent();

    public RestApiView(HistoryApi historyApi) {
        // TODO add additional rest apis
        Collection<RestApiItem> restApiItems = Arrays.stream(HistoryApi.class.getMethods())
                .filter(m -> m.isAnnotationPresent(RequestMapping.class))
                .map(method -> new RestApiItem(historyApi, new SwaggerRestApiMethod(method)))
                .toList();

        ComboBox<RestApiItem> comboBox = new ComboBox<>(new HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<ComboBox<RestApiItem>, RestApiItem>>() {
            @Override
            public void valueChanged(AbstractField.ComponentValueChangeEvent<ComboBox<RestApiItem>, RestApiItem> event) {
                if (event.getValue() == null) {
                    restEndpointComponent.unbind();
                } else {
                    restEndpointComponent.bind(event.getValue().getInstance(), event.getValue().getApiMethod());
                }
            }
        });
        comboBox.setItems(restApiItems);
        comboBox.setItemLabelGenerator(item -> item.getApiMethod().name());
        add(comboBox);

        add(restEndpointComponent);

        // TODO add component for showing the result
        // TODO add option to batch requests
    }

    @Getter
    @AllArgsConstructor
    protected static class RestApiItem {
        private final Object instance;
        private final RestApiMethod apiMethod;
    }
}
