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
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import dev.skrock.camunda.toolkit.api.ProcessDefinitionService;
import dev.skrock.camunda.toolkit.model.ProcessDefinition;
import dev.skrock.camunda.toolkit.util.ResponseException;

import java.util.Collections;
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

        H2 title = new H2("Process definitions");
        DownloadProcessDefinitionsButton downloadAllButton = new DownloadProcessDefinitionsButton(processDefinitionService);
        HorizontalLayout header = new HorizontalLayout(title, downloadAllButton);

        ProcessDefinitionGrid processDefinitionGrid = new ProcessDefinitionGrid(
                definition -> {
                    DownloadProcessDefinitionButton downloadButton = new DownloadProcessDefinitionButton(processDefinitionService);
                    downloadButton.setProcessDefinition(definition);
                    return downloadButton;
                }
        );

        Button exportButton = new Button("Export");
        exportButton.addClickListener(click -> {
            try {
                ExportArguments arguments = new ExportArguments();
                argumentsEditor.getBinder().writeBean(arguments);

                Set<ProcessDefinition> definitions = processDefinitionService.getProcessDefinitions();
                processDefinitionGrid.setProcessDefinitions(definitions);
                downloadAllButton.setProcessDefinitions(definitions);
            } catch (ValidationException e) {
                // TODO handle validation errors
            } catch (ResponseException e) {
                processDefinitionGrid.setProcessDefinitions(Collections.emptyList());
                downloadAllButton.setProcessDefinitions(Collections.emptyList());
                // TODO handle execution errors
            }
        });

        add(argumentsEditor, exportButton, header, processDefinitionGrid);
    }
}
