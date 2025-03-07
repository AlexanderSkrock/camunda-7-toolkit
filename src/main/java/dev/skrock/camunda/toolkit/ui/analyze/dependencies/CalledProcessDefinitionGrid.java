package dev.skrock.camunda.toolkit.ui.analyze.dependencies;

import com.vaadin.flow.component.grid.Grid;
import dev.skrock.camunda.toolkit.model.CalledProcessDefinition;

import java.util.*;

public class CalledProcessDefinitionGrid extends Grid<CalledProcessDefinition> {

    public CalledProcessDefinitionGrid(DependencyType dependencyType) {
        super(CalledProcessDefinition.class, false);
        if (dependencyType == DependencyType.OUTGOING) {
            addColumn(CalledProcessDefinition::getId).setHeader("ID");
            addColumn(CalledProcessDefinition::getKey).setHeader("Key");
            addColumn(CalledProcessDefinition::getVersion).setHeader("Version");
            addColumn(CalledProcessDefinition::getVersionTag).setHeader("Version Tag");
            addColumn(CalledProcessDefinition::getName).setHeader("Name");
            addColumn(CalledProcessDefinition::getDescription).setHeader("Description");
            addColumn(CalledProcessDefinition::getCalledFromActivityIds).setHeader("Calling activities");
        } else if (dependencyType == DependencyType.INCOMING) {
            addColumn(CalledProcessDefinition::getCallingProcessDefinitionId).setHeader("ID");
            addColumn(CalledProcessDefinition::getCalledFromActivityIds).setHeader("Calling activities");
        }
    }

    public void setDefinitions(Set<CalledProcessDefinition> calledProcessDefinitions) {
        setItems(calledProcessDefinitions);
    }
}
