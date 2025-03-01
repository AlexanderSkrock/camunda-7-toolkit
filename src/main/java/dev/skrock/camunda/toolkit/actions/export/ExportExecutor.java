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

package dev.skrock.camunda.toolkit.actions.export;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.FileUtils;
import org.camunda.community.rest.client.api.ProcessDefinitionApi;
import org.camunda.community.rest.client.model.ProcessDefinitionDiagramDto;
import org.camunda.community.rest.client.model.ProcessDefinitionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import dev.skrock.camunda.toolkit.actions.errors.ExecutionException;
import dev.skrock.camunda.toolkit.actions.ToolkitActionExecutor;
import dev.skrock.camunda.toolkit.actions.ToolkitActionType;
import dev.skrock.camunda.toolkit.actions.errors.RestExecutionException;
import dev.skrock.camunda.toolkit.actions.model.ProcessDefinitionMeta;
import lombok.extern.slf4j.Slf4j;

/**
 * <Beschreibung>
 * <br>
 *
 * @author askrock
 */
@Slf4j
@Component
public class ExportExecutor implements ToolkitActionExecutor {

    private final ProcessDefinitionApi processDefinitionApi;

    @Autowired
    public ExportExecutor(ProcessDefinitionApi processDefinitionApi) {
        this.processDefinitionApi = processDefinitionApi;
    }

    @Override
    public ToolkitActionType getActionType() {
        return ToolkitActionType.EXPORT;
    }

    @Override
    public void execute(Map<String, Object> args) throws ExecutionException {
        ExportArguments exportArgs = ExportArgumentsBinder.bind(args);

        if (exportArgs.getIncludes().isEmpty()) {
            log.warn("nothing selected to export, probably a miss configuration of the include argument");
            return;
        }

        if (exportArgs.getIncludes().contains(Includes.PROCESS_DEFINITION)) {
            exportProcessDefinitions(exportArgs);
        }
    }

    protected void exportProcessDefinitions(ExportArguments exportArgs) throws ExecutionException {

        ResponseEntity<List<ProcessDefinitionDto>> definitionsResponse = processDefinitionApi.getProcessDefinitions(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        RestExecutionException.checkResponse(definitionsResponse);

        List<ProcessDefinitionDto> definitions = definitionsResponse.getBody();
        if (definitions == null || definitions.isEmpty()) {
            log.info("no process definitions found");
            return;
        }

        for (int i = 0; i < definitions.size(); i++) {
            ProcessDefinitionDto currentDefinition = definitions.get(i);
            log.info("exporting definition {} of {}", i + 1, definitions.size());

            Path definitionPath = exportArgs.getOutput().resolve("processDefinitions").resolve(currentDefinition.getKey());
            if (!definitionPath.toFile().exists()) {
                definitionPath.toFile().mkdirs();
            }

            ResponseEntity<ProcessDefinitionDiagramDto> xmlResponse = processDefinitionApi.getProcessDefinitionBpmn20Xml(currentDefinition.getId());

            RestExecutionException.checkResponse(xmlResponse);

            Path metaPath = definitionPath.resolve("%s_%s.json".formatted(currentDefinition.getKey(), currentDefinition.getVersion()));
            try {
                ProcessDefinitionMeta meta = ProcessDefinitionMeta.builder()
                                                                  .processDefinitionKey(currentDefinition.getKey())
                                                                  .processDefinitionVersion(currentDefinition.getVersion())
                                                                  .build();
                String metaString = new ObjectMapper().writeValueAsString(meta);
                FileUtils.writeStringToFile(metaPath.toFile(), metaString, StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new ExecutionException("""
                error on writing the meta information:
                \tProcess Definition Key: %s
                \tversion: %s
                """.formatted(currentDefinition.getKey(), currentDefinition.getVersion()), e);
            }

            Path bpmnPath = definitionPath.resolve("%s_%s.bpmn".formatted(currentDefinition.getKey(), currentDefinition.getVersion()));
            try {
                FileUtils.writeStringToFile(bpmnPath.toFile(), xmlResponse.getBody().getBpmn20Xml(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new ExecutionException("""
                error on writing the xml:
                \tProcess Definition Key: %s
                \tversion: %s
                """.formatted(currentDefinition.getKey(), currentDefinition.getVersion()), e);
            }

            log.info("exported definition {} of {}", i + 1, definitions.size());
        }
    }
}
