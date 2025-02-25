package dev.skrock.camunda.ui;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import dev.skrock.camunda.actions.ToolkitActionsExecutor;
import org.springframework.beans.factory.annotation.Autowired;

@Route
public class MainView extends VerticalLayout {

    @Autowired
    public MainView(ToolkitActionsExecutor executor) {
        add(new Button("Execute", (ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> executor.executeActions()));
    }
}
