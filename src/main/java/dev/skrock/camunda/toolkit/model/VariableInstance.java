package dev.skrock.camunda.toolkit.model;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.With;
import org.camunda.community.rest.client.model.HistoricVariableInstanceDto;
import org.camunda.community.rest.client.model.VariableInstanceDto;
import org.springframework.util.unit.DataSize;

import java.nio.charset.StandardCharsets;

@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class VariableInstance {

    public static VariableInstance ofDto(VariableInstanceDto dto) {
        return new VariableInstance(
                dto.getActivityInstanceId(),
                dto.getName(),
                dto.getValue()
        );
    }

    public static VariableInstance ofDto(HistoricVariableInstanceDto dto) {
        return new VariableInstance(
                dto.getActivityInstanceId(),
                dto.getName(),
                dto.getValue()
        );
    }

    @With
    @EqualsAndHashCode.Include
    String variableInstanceId;

    @With
    String variableName;

    @With
    Object value;

    public DataSize getVariableSize() {
        if (getValue() == null) {
            return DataSize.ofBytes(0);
        }

        return DataSize.ofBytes(getValue().toString().getBytes(StandardCharsets.UTF_8).length);
    }
}
