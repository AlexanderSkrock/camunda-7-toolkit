package dev.skrock.camunda.toolkit.model;

import java.util.List;

import org.camunda.community.rest.client.model.CalledProcessDefinitionDto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.With;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class CalledProcessDefinition extends ProcessDefinition {

    public static CalledProcessDefinition ofDto(CalledProcessDefinitionDto dto) {
        return new CalledProcessDefinition(
                dto.getId(),
                dto.getKey(),
                dto.getCategory(),
                dto.getDescription(),
                dto.getName(),
                dto.getVersion(),
                dto.getVersionTag(),
                dto.getCalledFromActivityIds(),
                dto.getCallingProcessDefinitionId()
        );
    }

    @With
    @EqualsAndHashCode.Include
    String id;

    @With
    String key;

    @With
    String category;

    @With
    String description;

    @With
    String name;

    @With
    Integer version;

    @With
    String versionTag;

    @With
    List<String> calledFromActivityIds;

    @With
    @EqualsAndHashCode.Include
    String callingProcessDefinitionId;
}
