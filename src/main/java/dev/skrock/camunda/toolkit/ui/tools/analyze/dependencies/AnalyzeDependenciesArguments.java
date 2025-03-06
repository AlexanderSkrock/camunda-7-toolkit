package dev.skrock.camunda.toolkit.ui.tools.analyze.dependencies;

import lombok.Data;

@Data
public class AnalyzeDependenciesArguments {

    private String processDefinitionId;

    private boolean includeTransitive = false;
}
