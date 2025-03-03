package dev.skrock.camunda.toolkit.model;

import lombok.Builder;
import lombok.Value;
import lombok.With;
import lombok.extern.jackson.Jacksonized;
import org.camunda.community.rest.client.model.ProcessDefinitionDto;

@Value
@Builder
@Jacksonized
public class ProcessDefinition {

    public static ProcessDefinition ofDto(ProcessDefinitionDto dto) {
        return new ProcessDefinition(
                dto.getId(),
                dto.getKey(),
                dto.getCategory(),
                dto.getDescription(),
                dto.getName(),
                dto.getVersion(),
                dto.getVersionTag()
        );
    }

    @With
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
}
