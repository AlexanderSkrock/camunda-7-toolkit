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

package dev.skrock.camunda.toolkit.ui.tools.analyze.dependencies;

import com.vaadin.flow.data.binder.ValidationException;
import dev.skrock.camunda.toolkit.model.CalledProcessDefinition;
import dev.skrock.camunda.toolkit.model.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import dev.skrock.camunda.toolkit.api.ProcessDefinitionService;
import dev.skrock.camunda.toolkit.util.ResponseException;

import java.util.Set;

/**
 * <Beschreibung>
 * <br>
 *
 * @author askrock
 */
@Route("/tools/analyze/dependencies")
public class AnalyzeDependenciesToolView extends VerticalLayout {

    @Autowired
    public AnalyzeDependenciesToolView(ProcessDefinitionService processDefinitionService) {
        AnalyzeDependenciesArgumentsEditor argumentsEditor = new AnalyzeDependenciesArgumentsEditor();

        CalledProcessDefinitionGrid incomingDependencyGrid = new CalledProcessDefinitionGrid(DependencyType.INCOMING);
        CalledProcessDefinitionGrid outgoingDependencyGrid = new CalledProcessDefinitionGrid(DependencyType.OUTGOING);

        Button analyzeOutgoingButton = new Button("Analyze outgoing dependencies");
        analyzeOutgoingButton.addClickListener(click -> {
            try {
                AnalyzeDependenciesArguments arguments = new AnalyzeDependenciesArguments();
                argumentsEditor.getBinder().writeBean(arguments);

                ProcessDefinition currentDefinition = processDefinitionService.getProcessDefinition(arguments.getProcessDefinitionId());
                Set<CalledProcessDefinition> outgoingReferences = processDefinitionService.getOutgoingReferences(currentDefinition);
                outgoingDependencyGrid.setDefinitions(outgoingReferences);
            } catch (ValidationException e) {
                // TODO handle execution errors
            } catch (ResponseException e) {
                // TODO handle execution errors
            }
        });

        Button analyzeIncomingButton = new Button("Analyze incoming dependencies");
        analyzeIncomingButton.addClickListener(click -> {
            try {
                AnalyzeDependenciesArguments arguments = new AnalyzeDependenciesArguments();
                argumentsEditor.getBinder().writeBean(arguments);

                ProcessDefinition currentDefinition = processDefinitionService.getProcessDefinition(arguments.getProcessDefinitionId());
                Set<CalledProcessDefinition> incomingReferences = processDefinitionService.getIncomingReferences(currentDefinition);
                incomingDependencyGrid.setDefinitions(incomingReferences);
            } catch (ValidationException e) {
                // TODO handle execution errors
            } catch (ResponseException e) {
                // TODO handle execution errors
            }
        });

        add(argumentsEditor, analyzeOutgoingButton, outgoingDependencyGrid, analyzeIncomingButton, incomingDependencyGrid);
    }
}
