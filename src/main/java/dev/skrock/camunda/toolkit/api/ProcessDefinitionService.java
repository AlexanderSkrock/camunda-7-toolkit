package dev.skrock.camunda.toolkit.api;

import dev.skrock.camunda.toolkit.model.ProcessDefinition;
import dev.skrock.camunda.toolkit.model.ProcessDefinitionModel;
import dev.skrock.camunda.toolkit.util.ResponseException;
import dev.skrock.camunda.toolkit.util.ResponseUtil;
import org.camunda.community.rest.client.api.ProcessDefinitionApi;
import org.camunda.community.rest.client.model.ProcessDefinitionDiagramDto;
import org.camunda.community.rest.client.model.ProcessDefinitionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProcessDefinitionService {

    private final ProcessDefinitionApi processDefinitionApi;

    @Autowired
    public ProcessDefinitionService(ProcessDefinitionApi processDefinitionApi) {
        this.processDefinitionApi = processDefinitionApi;
    }

    public Set<ProcessDefinition> getProcessDefinitions() throws ResponseException {
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

        return Optional.ofNullable(definitionsResponse.getBody())
                .map(defs -> defs.stream().map(ProcessDefinition::ofDto).collect(Collectors.toSet()))
                .orElseGet(Collections::emptySet);
    }

    public ProcessDefinitionModel getProcessDefinitionModel(ProcessDefinition definition) throws ResponseException {
        ResponseEntity<ProcessDefinitionDiagramDto> diagramResponse = processDefinitionApi.getProcessDefinitionBpmn20Xml(definition.getId());
        ResponseUtil.checkResponse(diagramResponse);

        return ProcessDefinitionModel.ofDto(diagramResponse.getBody());
    }
}
