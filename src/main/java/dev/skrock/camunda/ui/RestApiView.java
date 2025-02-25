package dev.skrock.camunda.ui;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.camunda.community.rest.client.api.HistoryApi;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.Arrays;

@Route("/rest")
public class RestApiView extends VerticalLayout {

    public RestApiView(HistoryApi historyApi) {
        Method method = Arrays.stream(HistoryApi.class.getMethods())
                .filter(m -> m.isAnnotationPresent(RequestMapping.class))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("no supported method found"));

        // TODO add selection as drawer
        // TODO add additional rest apis
        add(new RestEndpointComponent(method, historyApi));

        // TODO add component for showing the result
        // TODO add option to batch requests
    }

}
