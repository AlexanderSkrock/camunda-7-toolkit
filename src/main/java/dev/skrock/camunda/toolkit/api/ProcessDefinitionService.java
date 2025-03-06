package dev.skrock.camunda.toolkit.api;

import dev.skrock.camunda.toolkit.model.CalledProcessDefinition;
import dev.skrock.camunda.toolkit.model.ProcessDefinition;
import dev.skrock.camunda.toolkit.model.ProcessDefinitionModel;
import dev.skrock.camunda.toolkit.util.ResponseException;
import dev.skrock.camunda.toolkit.util.ResponseUtil;
import org.camunda.community.rest.client.api.ProcessDefinitionApi;
import org.camunda.community.rest.client.model.CalledProcessDefinitionDto;
import org.camunda.community.rest.client.model.DeploymentWithDefinitionsDto;
import org.camunda.community.rest.client.model.ProcessDefinitionDiagramDto;
import org.camunda.community.rest.client.model.ProcessDefinitionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProcessDefinitionService {

    private final ProcessDefinitionApi processDefinitionApi;

    @Autowired
    public ProcessDefinitionService(ProcessDefinitionApi processDefinitionApi) {
        this.processDefinitionApi = processDefinitionApi;
    }

    public ProcessDefinition getProcessDefinition(String processDefinitionId) throws ResponseException {
        ResponseEntity<ProcessDefinitionDto> definitionResponse = processDefinitionApi.getProcessDefinition(processDefinitionId);
        ResponseUtil.checkResponse(definitionResponse);

        return ProcessDefinition.ofDto(definitionResponse.getBody());
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

    public List<ProcessDefinitionModel> getProcessDefinitionModels(Set<ProcessDefinition> definitions) throws ResponseException {
        List<ProcessDefinitionModel> models = new LinkedList<>();
        for (ProcessDefinition definition : definitions) {
            models.add(getProcessDefinitionModel(definition));
        }
        return models;
    }

    public Set<CalledProcessDefinition> getOutgoingReferences(ProcessDefinition processDefinition) throws ResponseException {
        return getOutgoingReferences(processDefinition, new LinkedMultiValueMap<>());
    }

    public Set<CalledProcessDefinition> getIncomingReferences(ProcessDefinition processDefinition) throws ResponseException {
        return getIncomingReferences(processDefinition, getProcessDefinitions(), new LinkedMultiValueMap<>());
    }

    protected Set<CalledProcessDefinition> getOutgoingReferences(ProcessDefinition definition, MultiValueMap<String, CalledProcessDefinition> cache) throws ResponseException {
        if (!cache.containsKey(definition.getId())) {
            ResponseEntity<List<CalledProcessDefinitionDto>> staticDependenciesResponse = processDefinitionApi.getStaticCalledProcessDefinitions(definition.getId());
            ResponseUtil.checkResponse(staticDependenciesResponse);
            if (CollectionUtils.isEmpty(staticDependenciesResponse.getBody())) {
                cache.put(definition.getId(), new LinkedList<>());
            } else {
                List<CalledProcessDefinition> calledDefinitions = staticDependenciesResponse.getBody().stream().map(CalledProcessDefinition::ofDto).collect(Collectors.toCollection(LinkedList::new));
                cache.put(definition.getId(), calledDefinitions);
            }
        }

        return new HashSet<>(cache.get(definition.getId()));
    }

    protected Set<CalledProcessDefinition> getIncomingReferences(ProcessDefinition definition, Set<ProcessDefinition> definitions, MultiValueMap<String, CalledProcessDefinition> cache) throws ResponseException {
        Set<CalledProcessDefinition> incomingReferences = new HashSet<>();

        for (ProcessDefinition currentDefinition : definitions) {
            if (!cache.containsKey(currentDefinition.getId())) {
                Set<CalledProcessDefinition> currentCalledDefinitions = getOutgoingReferences(currentDefinition, cache);
                currentCalledDefinitions.forEach(called -> cache.add(currentDefinition.getId(), called));
            }
            cache.get(currentDefinition.getId())
                    .stream()
                    .filter(calledDef -> Objects.equals(calledDef.getId(), definition.getId()))
                    .forEach(incomingReferences::add);
        }

        return incomingReferences;
    }
}
