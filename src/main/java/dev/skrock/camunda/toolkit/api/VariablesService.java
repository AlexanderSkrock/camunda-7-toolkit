package dev.skrock.camunda.toolkit.api;

import dev.skrock.camunda.toolkit.model.VariableInstance;
import dev.skrock.camunda.toolkit.util.ResponseException;
import dev.skrock.camunda.toolkit.util.ResponseUtil;
import org.camunda.community.rest.client.api.HistoryApi;
import org.camunda.community.rest.client.model.HistoricVariableInstanceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.unit.DataSize;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@Service
public class VariablesService {

    private final HistoryApi historyApi;

    @Autowired
    public VariablesService(HistoryApi historyApi) {
        this.historyApi = historyApi;
    }

    public List<VariableInstance> getVariables(String processInstanceId) throws ResponseException {
        ResponseEntity<List<HistoricVariableInstanceDto>> historicVariablesResponse = historyApi.getHistoricVariableInstances(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                processInstanceId,
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

        ResponseUtil.checkResponse(historicVariablesResponse);

        List<HistoricVariableInstanceDto> variableDtos = historicVariablesResponse.getBody();
        if (CollectionUtils.isEmpty(variableDtos)) {
            return Collections.emptyList();
        }

        return variableDtos.stream().map(VariableInstance::ofDto).sorted(Comparator.comparing(VariableInstance::getVariableSize).reversed()).toList();
    }

    public List<VariableInstance> getVariablesWithSize(String processInstance, DataSize minSize) throws ResponseException {
        return getVariables(processInstance)
                .stream()
                .filter(variable -> variable.getVariableSize().compareTo(minSize) >= 0)
                .toList();
    }
}
