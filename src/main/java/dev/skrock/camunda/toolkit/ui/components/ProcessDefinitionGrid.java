package dev.skrock.camunda.toolkit.ui.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import dev.skrock.camunda.toolkit.model.ProcessDefinition;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProcessDefinitionGrid extends TreeGrid<ProcessDefinition> {

    public ProcessDefinitionGrid(Function<ProcessDefinition, Component>... actionProviders) {
        super(ProcessDefinition.class, false);
        addHierarchyColumn(ProcessDefinition::getKey).setHeader("Key");
        addColumn(ProcessDefinition::getVersion).setHeader("Version");
        addColumn(ProcessDefinition::getVersionTag).setHeader("Version Tag");
        addColumn(ProcessDefinition::getDescription).setHeader("Description");

        if (ArrayUtils.isNotEmpty(actionProviders)) {
            addColumn(new ComponentRenderer<>(HorizontalLayout::new, (layout, definition) -> {
                for (Function<ProcessDefinition, Component> actionProvider : actionProviders) {
                    Component component = actionProvider.apply(definition);
                    layout.add(component);
                }
            })).setHeader("Actions");
        }
    }

    public void setProcessDefinitions(Collection<ProcessDefinition> definitions) {
        Map<String, TreeSet<ProcessDefinition>> groupedDefinitions = definitions.stream().collect(Collectors.groupingBy(
                ProcessDefinition::getKey,
                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(ProcessDefinition::getVersion)))
        ));

        TreeData<ProcessDefinition> data = new TreeData<>();
        groupedDefinitions.forEach((key, versions) -> {
            ProcessDefinition newestVersion = versions.last();
            data.addRootItems(newestVersion);
            versions.headSet(newestVersion).forEach(version -> {
                data.addItem(newestVersion, version);
            });
        });
        setDataProvider(new TreeDataProvider<>(data));
    }
}
