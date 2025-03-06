package dev.skrock.camunda.toolkit.model;

import lombok.*;

import org.camunda.community.rest.client.model.ProcessDefinitionDto;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProcessDefinition {

    public static ProcessDefinition ofDto(ProcessDefinitionDto dto) {
        ProcessDefinition definition = new ProcessDefinition();
        definition.setId(dto.getId());
        definition.setKey(dto.getKey());
        definition.setCategory(dto.getCategory());
        definition.setDescription(dto.getDescription());
        definition.setName(dto.getName());
        definition.setVersion(dto.getVersion());
        definition.setVersionTag(dto.getVersionTag());
        return definition;
    }

    @EqualsAndHashCode.Include
    private String id;

    private String key;

    private String category;

    private String description;

    private String name;

    private Integer version;

    private String versionTag;
}
