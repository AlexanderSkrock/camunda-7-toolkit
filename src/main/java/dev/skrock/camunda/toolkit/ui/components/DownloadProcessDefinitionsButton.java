package dev.skrock.camunda.toolkit.ui.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import dev.skrock.camunda.toolkit.api.ProcessDefinitionService;
import dev.skrock.camunda.toolkit.model.ProcessDefinition;

import java.util.Collection;

public class DownloadProcessDefinitionsButton extends Anchor {

    public DownloadProcessDefinitionsButton(ProcessDefinitionService processDefinitionService, Collection<ProcessDefinition> definitions) {
        Button downloadButton = new Button();
        downloadButton.setIcon(new Icon(VaadinIcon.DOWNLOAD));
        downloadButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
        add(downloadButton);

        // Build zip file containing all definitions

        getElement().setAttribute("download", true);
    }
}
