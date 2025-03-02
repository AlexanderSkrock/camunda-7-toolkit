package dev.skrock.camunda.toolkit.model;

import lombok.Data;
import org.camunda.community.rest.client.model.ProcessDefinitionDiagramDto;

@Data
public class ProcessDefinitionModel {

    public static ProcessDefinitionModel ofDto(ProcessDefinitionDiagramDto dto) {
        ProcessDefinitionModel model = new ProcessDefinitionModel();
        model.setProcessDefinitionId(dto.getId());
        model.setXml(dto.getBpmn20Xml());
        return model;
    }

    private String processDefinitionId;
    private String xml;
}
