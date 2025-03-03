package dev.skrock.camunda.toolkit.ui.components;

import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import dev.skrock.camunda.toolkit.api.ProcessDefinitionService;
import dev.skrock.camunda.toolkit.model.ProcessDefinition;
import dev.skrock.camunda.toolkit.model.ProcessDefinitionModel;
import dev.skrock.camunda.toolkit.util.ResponseException;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class DownloadProcessDefinitionsButton extends DownloadButton {

    private final ProcessDefinitionService processDefinitionService;

    private final Set<ProcessDefinition> definitions = new HashSet<>();

    public DownloadProcessDefinitionsButton(ProcessDefinitionService processDefinitionService) {
        super();
        this.processDefinitionService = processDefinitionService;
    }

    public void setProcessDefinitions(Collection<ProcessDefinition> definitions) {
        this.definitions.clear();
        this.definitions.addAll(definitions);
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
        if (CollectionUtils.isEmpty(definitions)) {
            return null;
        }
        String resourceName = "processDefinitions_%s.zip".formatted(DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now()));
        return new StreamResource(resourceName, (InputStreamFactory) () -> {
            try (ByteArrayOutputStream zipBytes = new ByteArrayOutputStream()) {
                try (ZipOutputStream zip = new ZipOutputStream(zipBytes)) {
                    for (ProcessDefinitionModel model : processDefinitionService.getProcessDefinitionModels(definitions)) {
                        ProcessDefinition definition = definitions.stream().filter(def -> Objects.equals(def.getId(), model.getProcessDefinitionId())).findFirst().orElseThrow();
                        String fileName = "%s_%s.bpmn".formatted(definition.getKey(), definition.getVersion());
                        ZipEntry zipEntry = new ZipEntry(fileName);
                        zip.putNextEntry(zipEntry);
                        zip.write(model.getXml().getBytes(StandardCharsets.UTF_8));
                        zip.closeEntry();
                    }
                }
                return new ByteArrayInputStream(zipBytes.toByteArray());
            } catch (ResponseException | IOException e) {
                // TODO add error handling
                throw new RuntimeException(e);
            }
        });
    }
}
