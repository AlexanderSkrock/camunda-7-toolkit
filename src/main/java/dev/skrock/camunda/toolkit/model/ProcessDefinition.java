package dev.skrock.camunda.toolkit.model;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.With;
import org.camunda.community.rest.client.model.ProcessDefinitionDto;

@Value
@AllArgsConstructor
public class ProcessDefinition {

    public static ProcessDefinition ofDto(ProcessDefinitionDto dto) {
        return new ProcessDefinition(
                dto.getId(),
                dto.getKey(),
                dto.getCategory(),
                dto.getDescription(),
                dto.getName(),
                dto.getVersion(),
                dto.getResource(),
                dto.getDeploymentId(),
                dto.getDiagram(),
                dto.getSuspended(),
                dto.getTenantId(),
                dto.getVersionTag(),
                dto.getHistoryTimeToLive(),
                dto.getStartableInTasklist()
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
    String resource;

    @With
    String deploymentId;

    @With
    String diagram;

    @With
    Boolean suspended;

    @With
    String tenantId;

    @With
    String versionTag;

    @With
    Integer historyTimeToLive;

    @With
    Boolean startableInTasklist;
}
