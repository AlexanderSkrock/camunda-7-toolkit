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

package dev.skrock.camunda.toolkit.ui.tools;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;

/**
 * <Beschreibung>
 * <br>
 *
 * @author askrock
 */
@Route("/tools/export")
public class ExportToolView extends VerticalLayout {

    public ExportToolView() {
        ExportArgumentsEditor argumentsEditor = new ExportArgumentsEditor();

        Button exportButton = new Button("Export");
        exportButton.addClickListener(click -> {
            try {
                ExportArguments arguments = new ExportArguments();
                argumentsEditor.getBinder().writeBean(arguments);
                // TODO actually execute the export
            } catch (ValidationException e) {
                // TODO handle validation errors
            }
        });

        add(argumentsEditor, exportButton);
    }
}
