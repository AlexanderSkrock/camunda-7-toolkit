/*
  ------------------------------------------------------------------------------
        (c) by data experts gmbh
              Postfach 1130
              Woldegker Str. 12
              17001 Neubrandenburg

  Dieses Dokument und die hierin enthaltenen Informationen unterliegen
  dem Urheberrecht und duerfen ohne die schriftliche Genehmigung des
  Herausgebers weder als ganzes noch in Teilen dupliziert oder reproduziert
  noch manipuliert werden.
*/

package dev.skrock.camunda.toolkit.ui.analyze.sap;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.function.Predicate;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import dev.skrock.camunda.toolkit.api.ModelService;
import dev.skrock.camunda.toolkit.api.ProcessDefinitionService;
import dev.skrock.camunda.toolkit.model.CalledSapFunction;
import dev.skrock.camunda.toolkit.model.CalledSapTable;
import dev.skrock.camunda.toolkit.model.ProcessDefinition;
import dev.skrock.camunda.toolkit.model.ProcessDefinitionModel;
import dev.skrock.camunda.toolkit.ui.Roles;
import dev.skrock.camunda.toolkit.util.ResponseException;
import jakarta.annotation.security.RolesAllowed;

/**
 * <Beschreibung>
 * <br>
 *
 * @author askrock
 */
@Route("/tools/analyze/sap")
@RolesAllowed(Roles.ANALYZE_ROLE)
public class AnalyzeSapToolView extends VerticalLayout {

    @Autowired
    public AnalyzeSapToolView(ProcessDefinitionService processDefinitionService, ModelService modelService) {
        AnalyzeSapArgumentsEditor argumentsEditor = new AnalyzeSapArgumentsEditor();

        SapFunctionGrid sapFunctionGrid = new SapFunctionGrid();
        SapTableGrid sapTableGrid = new SapTableGrid();

        Button analyzeButton = new Button("Analyze");
        analyzeButton.addClickListener(click -> {
            try {
                AnalyzeSapArguments arguments = new AnalyzeSapArguments();
                argumentsEditor.getBinder().writeBean(arguments);

                ProcessDefinition definition = processDefinitionService.getProcessDefinition(arguments.getProcessDefinitionId());
                ProcessDefinitionModel model = processDefinitionService.getProcessDefinitionModel(definition);
                BpmnModelInstance modelInstance = Bpmn.readModelFromStream(new ByteArrayInputStream(model.getXml().getBytes(StandardCharsets.UTF_8)));
                Collection<CalledSapFunction> functions = modelService.analyzeSapCalls(modelInstance);

                Collection<CalledSapFunction> nonTableFunctions = functions
                        .stream()
                        .filter(Predicate.not(CalledSapTable.class::isInstance))
                        .toList();
                sapFunctionGrid.setItems(nonTableFunctions);

                Collection<CalledSapTable> tableFunctions = functions
                        .stream()
                        .filter(CalledSapTable.class::isInstance)
                        .map(CalledSapTable.class::cast)
                        .toList();
                sapTableGrid.setItems(tableFunctions);
            } catch (ValidationException e) {
                // TODO handle execution errors
            } catch (ResponseException e) {
                // TODO handle execution errors
            }
        });

        TabSheet resultTabs = new TabSheet();
        resultTabs.setWidthFull();
        resultTabs.add("Functions", sapFunctionGrid);
        resultTabs.add("Tables", sapTableGrid);

        add(argumentsEditor, analyzeButton, resultTabs);
    }
}
