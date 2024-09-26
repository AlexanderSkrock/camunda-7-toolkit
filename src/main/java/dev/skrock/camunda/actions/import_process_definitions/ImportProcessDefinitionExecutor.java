/*
  ------------------------------------------------------------------------------
        (c) by data experts gmbh
              Postfach 1130
              Woldegker Str. 12
              17001 Neubrandenburg

  Dieses Dokument und die hierin enthaltenen Informationen unterliegen
  dem Urheberrecht und duerfen ohne die schriftliche Genehmigung des
  Herausgebers weder als ganzes noch in Teilen dupliziert oder reproduziert
  noch manipuliert werden.
*/

package dev.skrock.camunda.actions.import_process_definitions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.camunda.community.rest.client.api.DeploymentApi;
import org.camunda.community.rest.client.api.ProcessDefinitionApi;
import org.camunda.community.rest.client.model.DeploymentDto;
import org.camunda.community.rest.client.model.DeploymentWithDefinitionsDto;
import org.camunda.community.rest.client.model.ProcessDefinitionDiagramDto;
import org.camunda.community.rest.client.model.ProcessDefinitionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import dev.skrock.camunda.actions.errors.ExecutionException;
import dev.skrock.camunda.actions.ToolkitActionExecutor;
import dev.skrock.camunda.actions.ToolkitActionType;
import dev.skrock.camunda.actions.errors.RestExecutionException;
import dev.skrock.camunda.actions.export.Includes;
import dev.skrock.camunda.actions.model.ProcessDefinitionMeta;
import lombok.extern.slf4j.Slf4j;

/**
 * <Beschreibung>
 * <br>
 *
 * @author askrock
 */
@Slf4j
@Component
public class ImportProcessDefinitionExecutor implements ToolkitActionExecutor {

    private final DeploymentApi deploymentApi;

    @Autowired
    public ImportProcessDefinitionExecutor(DeploymentApi deploymentApi) {
        this.deploymentApi = deploymentApi;
    }

    @Override
    public ToolkitActionType getActionType() {
        return ToolkitActionType.IMPORT_PROCESS_DEFINITIONS;
    }

    @Override
    public void execute(Map<String, Object> args) throws ExecutionException {
        ImportProcessDefinitionArguments importArgs = ImportProcessDefinitionsArgumentsBinder.bind(args);

        if (importArgs.getInput() == null) {
            log.warn("nothing selected to import, probably a miss configuration of the input argument");
            return;
        }

        Set<ImportProcessDefinitionJob> jobs = collectImportJobs(importArgs);
        if (jobs.isEmpty()) {
            log.info("no process definitions found");
            return;
        }

        TreeSet<ImportProcessDefinitionJob> orderedJobs = new TreeSet<>(jobComparator());
        orderedJobs.addAll(jobs);

        AtomicInteger finishedJobs = new AtomicInteger(0);
        for (ImportProcessDefinitionJob currentJob : orderedJobs) {
            log.info("importing definition {} of {}", finishedJobs.get() + 1, orderedJobs.size());

            importProcessDefinition(currentJob.getXmlPath());

            int nextCount = finishedJobs.incrementAndGet();
            log.info("imported definition {} of {}", nextCount, orderedJobs.size());
        }
    }

    protected Set<ImportProcessDefinitionJob> collectImportJobs(ImportProcessDefinitionArguments args) throws ExecutionException {
        Set<ImportProcessDefinitionJob> jobs = new HashSet<>();

        FileVisitor<Path> jobCollectorVisitor = new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (FileUtils.isRegularFile(file.toFile())) {
                    String extension = FilenameUtils.getExtension(file.toString());
                    if ("bpmn".equals(extension)) {
                        String filename = FilenameUtils.getBaseName(file.toString());

                        Path metaPath = file.resolveSibling(filename + ".json");

                        if (metaPath.toFile().exists()) {
                            ProcessDefinitionMeta meta = new ObjectMapper().readValue(metaPath.toFile(), ProcessDefinitionMeta.class);
                            jobs.add(new ImportProcessDefinitionJob(meta, file));
                        } else {
                           jobs.add(new ImportProcessDefinitionJob(null, file));
                        }
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        };

        try {
            Files.walkFileTree(args.getInput(), jobCollectorVisitor);
        } catch (IOException e) {
            throw new ExecutionException("unable to collect import jobs", e);
        }

        return jobs;
    }

    protected Comparator<ImportProcessDefinitionJob> jobComparator() {
        return Comparator.comparing(
            ImportProcessDefinitionJob::getMeta,
            Comparator.nullsLast(
                    Comparator.comparing(ProcessDefinitionMeta::getProcessDefinitionKey)
                              .thenComparing(ProcessDefinitionMeta::getProcessDefinitionVersion)
            )
        );
    }

    protected void importProcessDefinition(Path bpmnPath) throws ExecutionException {
        ResponseEntity<DeploymentWithDefinitionsDto> deploymentResponse = deploymentApi.createDeployment(
                null,
                null,
                null,
                null,
                null,
                null,
                new MultipartFile[] { new PathMultipartFile(bpmnPath, MediaType.APPLICATION_XML) }
        );

        RestExecutionException.checkResponse(deploymentResponse);
    }
}
