package dev.skrock.camunda.toolkit.ui.analyze.variables;

import com.vaadin.flow.component.grid.Grid;
import dev.skrock.camunda.toolkit.model.VariableInstance;

public class VariablesGrid extends Grid<VariableInstance> {

    public VariablesGrid() {
        super(VariableInstance.class, false);
        addColumn(VariableInstance::getVariableInstanceId).setHeader("Variable Instance Id");
        addColumn(VariableInstance::getVariableName).setHeader("Name");
        addColumn(VariableInstance::getVariableSize).setHeader("Size");
    }
}
