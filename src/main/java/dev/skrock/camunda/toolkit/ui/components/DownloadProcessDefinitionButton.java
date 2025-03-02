package dev.skrock.camunda.toolkit.ui.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import dev.skrock.camunda.toolkit.api.ProcessDefinitionService;
import dev.skrock.camunda.toolkit.model.ProcessDefinition;
import dev.skrock.camunda.toolkit.util.ResponseException;
import org.springframework.http.MediaType;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class DownloadProcessDefinitionButton extends Anchor {

    public DownloadProcessDefinitionButton(ProcessDefinitionService processDefinitionService, ProcessDefinition definition) {
        Button downloadButton = new Button();
        downloadButton.setIcon(new Icon(VaadinIcon.DOWNLOAD));
        downloadButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
        add(downloadButton);

        String fileName = "%s_%s.bpmn".formatted(definition.getKey(), definition.getVersion());
        StreamResource xmlResource = new StreamResource(fileName, (InputStreamFactory) () -> {
            try {
                String xml = processDefinitionService.getProcessDefinitionModel(definition).getXml();
                return new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
            } catch (ResponseException e) {
                throw new RuntimeException(e);
            }
        });
        xmlResource.setContentType(MediaType.APPLICATION_XML_VALUE);
        setHref(xmlResource);

        getElement().setAttribute("download", true);
    }
}
