package dev.skrock.camunda.toolkit.ui.analyze.sap;

import lombok.Data;

@Data
public class AnalyzeSapArguments {

    private String processDefinitionId;

    private boolean includeTransitive = false;
}
