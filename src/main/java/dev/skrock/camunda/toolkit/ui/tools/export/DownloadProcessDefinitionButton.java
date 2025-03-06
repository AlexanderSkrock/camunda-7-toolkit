package dev.skrock.camunda.toolkit.ui.tools.export;

import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import dev.skrock.camunda.toolkit.api.ProcessDefinitionService;
import dev.skrock.camunda.toolkit.model.ProcessDefinition;
import dev.skrock.camunda.toolkit.ui.components.DownloadButton;
import dev.skrock.camunda.toolkit.util.ResponseException;
import org.springframework.http.MediaType;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class DownloadProcessDefinitionButton extends DownloadButton {

    private final ProcessDefinitionService processDefinitionService;

    private ProcessDefinition definition;

    public DownloadProcessDefinitionButton(ProcessDefinitionService processDefinitionService) {
        super();
        this.processDefinitionService = processDefinitionService;
    }

    public void setProcessDefinition(ProcessDefinition definition) {
        this.definition = definition;
        refreshHref();
    }

    protected void refreshHref() {
        if (getResource() != null) {
            setHref(getResource());
        } else {
            removeHref();
        }
    }

    protected StreamResource getResource() {
        if (definition == null) {
            return null;
        }

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
        return xmlResource;
    }
}
