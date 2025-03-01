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

package dev.skrock.camunda.toolkit.actions.analyze_variable_sizes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.camunda.community.rest.client.api.HistoryApi;
import org.camunda.community.rest.client.model.HistoricVariableInstanceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import dev.skrock.camunda.toolkit.actions.ToolkitActionExecutor;
import dev.skrock.camunda.toolkit.actions.ToolkitActionType;
import dev.skrock.camunda.toolkit.actions.errors.ExecutionException;
import dev.skrock.camunda.toolkit.actions.errors.RestExecutionException;
import lombok.extern.slf4j.Slf4j;

/**
 * <Beschreibung>
 * <br>
 *
 * @author askrock
 */
@Slf4j
@Component
public class AnalyzeVariableSizesExecutor implements ToolkitActionExecutor {

    private static final ObjectWriter JSON_WRITER = new ObjectMapper().writerWithDefaultPrettyPrinter();

    private final HistoryApi historyApi;

    @Autowired
    public AnalyzeVariableSizesExecutor(HistoryApi historyApi) {
        this.historyApi = historyApi;
    }

    @Override
    public ToolkitActionType getActionType() {
        return ToolkitActionType.ANALYZE_VARIABLE_SIZES;
    }

    @Override
    public void execute(Map<String, Object> args) throws ExecutionException {
        AnalyzeVariableSizesArguments arguments = AnalyzeVariableSizesArgumentsBinder.bind(args);

        ResponseEntity<List<HistoricVariableInstanceDto>> historicVariablesResponse = historyApi.getHistoricVariableInstances(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                arguments.getProcessInstanceId(),
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
                true
        );

        RestExecutionException.checkResponse(historicVariablesResponse);

        List<HistoricVariableInstanceDto> variables = historicVariablesResponse.getBody();
        if (variables == null || variables.isEmpty()) {
            log.info("no historic process variables found");
            return;
        }

        List<VariableSizeInfo> sizeInfos = new LinkedList<>();
        variables.forEach(variable -> sizeInfos.add(VariableSizeInfo.of(variable)));
        sizeInfos.sort(Comparator.comparing(VariableSizeInfo::getVariableSize).reversed());

        try {
            String variableSizesJson = JSON_WRITER.writeValueAsString(sizeInfos);
            Path outputPath = arguments.getOutput().resolve(arguments.getProcessInstanceId()).resolve("variableSizes.json");
            FileUtils.writeStringToFile(outputPath.toFile(), variableSizesJson, StandardCharsets.UTF_8);

            log.info("finished writing the sizes to file");
        } catch (IOException e) {
            log.error("error on writing the sizes to file", e);
        }

        variables.forEach(variable -> {
            if (VariablesSizes.getVariableSize(variable).compareTo(arguments.getMinSizeLimit()) < 0) {
                log.info("skipped writing '{}' due to file limits", variable.getName());
                return;
            }

            try {
                String variableValueJson = JSON_WRITER.writeValueAsString(variable.getValue());
                Path outputPath = arguments.getOutput().resolve(arguments.getProcessInstanceId()).resolve("variable-%s-%s.json".formatted(variable.getName(), variable.getId()));
                FileUtils.writeStringToFile(outputPath.toFile(), variableValueJson, StandardCharsets.UTF_8);

                log.info("finished writing '{}' to file", variable.getName());
            } catch (IOException e) {
                log.error("error on writing '{}' to file", variable.getName(), e);
            }
        });
    }
}
