package dev.skrock.camunda.toolkit.ui.transfer.imports;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.Route;
import dev.skrock.camunda.toolkit.api.DeploymentService;
import dev.skrock.camunda.toolkit.model.ProcessDefinition;
import dev.skrock.camunda.toolkit.ui.Roles;
import dev.skrock.camunda.toolkit.util.ResponseException;
import jakarta.annotation.security.RolesAllowed;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Route("/tools/import")
@RolesAllowed(Roles.DATA_TRANSFER_ROLE)
public class ImportToolView extends VerticalLayout {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    public ImportToolView(DeploymentService deploymentService) {
        MultiFileMemoryBuffer fileBuffer = new MultiFileMemoryBuffer();
        Upload uploadField = new Upload(fileBuffer);
        uploadField.setAcceptedFileTypes("application/zip", ".zip");
        uploadField.setMaxFiles(1);

        Button importButton = new Button("Import");
        importButton.addClickListener(click -> {
            if (!uploadField.isUploading() || CollectionUtils.isEmpty(fileBuffer.getFiles())) {
                for (String zipFileName : fileBuffer.getFiles()) {
                    Map<String, ProcessDefinition> fileNameToMeta = new HashMap<>();
                    Map<String, byte[]> fileNameToContent = new HashMap<>();

                    try (InputStream fileStream = fileBuffer.getInputStream(zipFileName);
                        ZipInputStream zipStream = new ZipInputStream(fileStream)) {

                        ZipEntry entry = null;
                        while ((entry = zipStream.getNextEntry()) != null) {
                            String entryName = entry.getName();
                            String identifier = FilenameUtils.getBaseName(entryName);
                            String extension = FilenameUtils.getExtension(entryName);

                            if ("bpmn".equals(extension)) {
                                byte[] bytes = zipStream.readAllBytes();
                                fileNameToContent.put(identifier, bytes);
                            } else if ("json".equals(extension)) {
                                byte[] bytes = zipStream.readAllBytes();
                                ProcessDefinition meta = MAPPER.readValue(bytes, ProcessDefinition.class);
                                fileNameToMeta.put(identifier, meta);
                            }
                            zipStream.closeEntry();
                        }
                    } catch (IOException e) {
                        // TODO handle error
                    }

                    TreeMap<String, byte[]> orderedFileNameToContent = new TreeMap<>(new Comparator<String>() {
                        @Override
                        public int compare(String name1, String name2) {
                            ProcessDefinition definition1 = fileNameToMeta.get(name1);
                            ProcessDefinition definition2 = fileNameToMeta.get(name2);

                            if (definition1 == null || definition2 == null) {
                                return Comparator.<String>naturalOrder().compare(name1, name2);
                            }

                            return Comparator.nullsLast(
                                            Comparator.comparing(ProcessDefinition::getKey)
                                                    .thenComparing(ProcessDefinition::getVersion)
                                    )
                                    .compare(definition1, definition2);
                        }
                    });
                    orderedFileNameToContent.putAll(fileNameToContent);

                    try {
                        for (Map.Entry<String, byte[]> entry : orderedFileNameToContent.entrySet()) {
                            String fileName = entry.getKey();
                            byte[] content = entry.getValue();
                            // TODO Currently we deploy tenant independent
                            deploymentService.deployDiagram(fileName, content);
                        }
                    } catch (ResponseException e) {
                        // TODO handle error
                    }
                }
            }
        });

        add(uploadField, importButton);
    }
}
