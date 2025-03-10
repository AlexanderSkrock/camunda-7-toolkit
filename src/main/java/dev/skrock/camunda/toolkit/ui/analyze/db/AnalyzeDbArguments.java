package dev.skrock.camunda.toolkit.ui.analyze.db;

import lombok.Data;

@Data
public class AnalyzeDbArguments {

    private String processDefinitionId;

    private boolean includeTransitive = false;
}
