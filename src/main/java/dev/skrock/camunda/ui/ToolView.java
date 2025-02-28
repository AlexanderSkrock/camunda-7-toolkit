package dev.skrock.camunda.ui;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import dev.skrock.camunda.actions.ToolkitActionsExecutor;
import org.springframework.beans.factory.annotation.Autowired;

@Route("/tool")
public class ToolView extends VerticalLayout {

    @Autowired
    public ToolView(ToolkitActionsExecutor executor) {
        // TODO split up in separate views with configuration
        add(new Button("Execute", (ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> executor.executeActions()));
    }
}
