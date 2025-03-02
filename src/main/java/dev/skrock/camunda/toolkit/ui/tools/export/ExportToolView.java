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

package dev.skrock.camunda.toolkit.ui.tools.export;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import dev.skrock.camunda.toolkit.api.ProcessDefinitionService;
import dev.skrock.camunda.toolkit.model.ProcessDefinition;
import dev.skrock.camunda.toolkit.ui.components.DownloadProcessDefinitionButton;
import dev.skrock.camunda.toolkit.ui.components.ProcessDefinitionGrid;
import dev.skrock.camunda.toolkit.util.ResponseException;

import java.util.Set;

/**
 * <Beschreibung>
 * <br>
 *
 * @author askrock
 */
@Route("/tools/export")
public class ExportToolView extends VerticalLayout {

    public ExportToolView(ProcessDefinitionService processDefinitionService) {
        ExportArgumentsEditor argumentsEditor = new ExportArgumentsEditor();

        ProcessDefinitionGrid processDefinitionGrid = new ProcessDefinitionGrid(
                definition -> new DownloadProcessDefinitionButton(processDefinitionService, definition)
        );

        Button exportButton = new Button("Export");
        exportButton.addClickListener(click -> {
            try {
                ExportArguments arguments = new ExportArguments();
                argumentsEditor.getBinder().writeBean(arguments);

                Set<ProcessDefinition> definitions = processDefinitionService.getProcessDefinitions();
                processDefinitionGrid.setProcessDefinitions(definitions);
            } catch (ValidationException e) {
                // TODO handle validation errors
            } catch (ResponseException e) {
                // TODO handle execution errors
            }
        });

        // TODO add download all button

        add(argumentsEditor, exportButton, processDefinitionGrid);
    }
}
