package dev.skrock.camunda.toolkit.ui.analyze.variables;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import dev.skrock.camunda.toolkit.api.VariablesService;
import dev.skrock.camunda.toolkit.model.VariableInstance;
import dev.skrock.camunda.toolkit.ui.Roles;
import dev.skrock.camunda.toolkit.util.ResponseException;
import jakarta.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

@Route("/tools/analyze/variables")
@RolesAllowed(Roles.ANALYZE_ROLE)
public class AnalyzeVariablesToolView extends VerticalLayout {

    @Autowired
    public AnalyzeVariablesToolView(VariablesService variablesService) {
        AnalyzeVariablesArgumentsEditor editor = new AnalyzeVariablesArgumentsEditor();

        VariablesGrid variablesGrid = new VariablesGrid();

        Button analyzeButton = new Button("Analyze");
        analyzeButton.addClickListener(click -> {
            try {
                AnalyzeVariablesArguments arguments = new AnalyzeVariablesArguments();
                editor.getBinder().writeBean(arguments);

                List<VariableInstance> variables = variablesService.getVariablesWithSize(arguments.getProcessInstanceId(), arguments.getMinSize());
                variablesGrid.setItems(variables);
            } catch (ValidationException e) {
                variablesGrid.setItems(Collections.emptyList());
                // TODO handle validation errors
            } catch (ResponseException e) {
                variablesGrid.setItems(Collections.emptyList());
                // TODO handle execution errors
            }
        });

        add(editor, analyzeButton, variablesGrid);
    }
}
