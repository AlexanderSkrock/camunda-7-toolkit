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
import dev.skrock.camunda.toolkit.model.ProcessDefinitionModel;
import dev.skrock.camunda.toolkit.util.ResponseException;
import org.springframework.http.MediaType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class DownloadProcessDefinitionsButton extends Anchor {

    private final ProcessDefinitionService processDefinitionService;

    private final Set<ProcessDefinition> definitions = new HashSet<>();

    public DownloadProcessDefinitionsButton(ProcessDefinitionService processDefinitionService) {
        this.processDefinitionService = processDefinitionService;

        Button downloadButton = new Button();
        downloadButton.setIcon(new Icon(VaadinIcon.DOWNLOAD));
        downloadButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
        add(downloadButton);

        getElement().setAttribute("download", true);
    }

    public void setDefinitions(Collection<ProcessDefinition> definitions) {
        this.definitions.clear();
        this.definitions.addAll(definitions);
        prepareDownload();
    }

    protected void prepareDownload() {
        if (definitions.isEmpty()) {
            removeHref();
        } else {
            String resourceName = "processDefinitions_%s.zip".formatted(DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now()));
            StreamResource resource = new StreamResource(resourceName, (InputStreamFactory) () -> {
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
            setHref(resource);
        }
    }
}
