package dev.skrock.camunda.toolkit.ui.analyze.dependencies;

import lombok.Data;

@Data
public class AnalyzeDependenciesArguments {

    private String processDefinitionId;

    private boolean includeTransitive = false;
}
