package dev.skrock.camunda.ui.tools;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("/tools")
public class ToolsView extends VerticalLayout {

    public ToolsView() {
        add(new Button("Export", (ComponentEventListener<ClickEvent<Button>>) event -> {
            event.getSource().getUI().ifPresent(ui -> ui.navigate(ExportToolView.class));
        }));
        // TODO add additional tools
    }
}
