package dev.skrock.camunda.toolkit.model;

import java.util.LinkedList;
import java.util.List;

import lombok.*;
import org.camunda.community.rest.client.model.CalledProcessDefinitionDto;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class CalledProcessDefinition extends ProcessDefinition {

    public static CalledProcessDefinition ofDto(CalledProcessDefinitionDto dto) {
        CalledProcessDefinition definition = new CalledProcessDefinition();
        definition.setId(dto.getId());
        definition.setKey(dto.getKey());
        definition.setCategory(dto.getCategory());
        definition.setDescription(dto.getDescription());
        definition.setName(dto.getName());
        definition.setVersion(dto.getVersion());
        definition.setVersionTag(dto.getVersionTag());
        definition.setCallingProcessDefinitionId(dto.getCallingProcessDefinitionId());
        definition.setCalledFromActivityIds(dto.getCalledFromActivityIds());
        return definition;
    }

    @EqualsAndHashCode.Include
    private String callingProcessDefinitionId;

    private List<String> calledFromActivityIds = new LinkedList<>();
}
