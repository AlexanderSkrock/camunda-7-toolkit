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

package dev.skrock.camunda.toolkit.ui.analyze.db;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import dev.skrock.camunda.toolkit.api.ModelService;
import dev.skrock.camunda.toolkit.api.ProcessDefinitionService;
import dev.skrock.camunda.toolkit.model.CalledDbQuery;
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
@Route("/tools/analyze/db")
@RolesAllowed(Roles.ANALYZE_ROLE)
public class AnalyzeDbToolView extends VerticalLayout {

    @Autowired
    public AnalyzeDbToolView(ProcessDefinitionService processDefinitionService, ModelService modelService) {
        AnalyzeDbArgumentsEditor argumentsEditor = new AnalyzeDbArgumentsEditor();

        DbQueryGrid dbQueryGrid = new DbQueryGrid();

        Button analyzeButton = new Button("Analyze");
        analyzeButton.addClickListener(click -> {
            try {
                AnalyzeDbArguments arguments = new AnalyzeDbArguments();
                argumentsEditor.getBinder().writeBean(arguments);

                ProcessDefinition definition = processDefinitionService.getProcessDefinition(arguments.getProcessDefinitionId());
                ProcessDefinitionModel model = processDefinitionService.getProcessDefinitionModel(definition);
                BpmnModelInstance modelInstance = Bpmn.readModelFromStream(new ByteArrayInputStream(model.getXml().getBytes(StandardCharsets.UTF_8)));
                Collection<CalledDbQuery> dbQueries = modelService.analyzeDbCalls(modelInstance);

                dbQueryGrid.setItems(dbQueries);
            } catch (ValidationException e) {
                // TODO handle execution errors
            } catch (ResponseException e) {
                // TODO handle execution errors
            }
        });

        add(argumentsEditor, analyzeButton, dbQueryGrid);
    }
}
