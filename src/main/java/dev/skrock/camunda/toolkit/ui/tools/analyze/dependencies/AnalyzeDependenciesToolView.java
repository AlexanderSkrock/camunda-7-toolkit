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

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import dev.skrock.camunda.toolkit.api.ProcessDefinitionService;
import dev.skrock.camunda.toolkit.util.ResponseException;

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
        // TODO implement dependency grid
        // TODO add option to invert dependency view
        // TODO add option to filter for a specific process definition

        Button analyzeButton = new Button("Analyze");
        analyzeButton.addClickListener(click -> {
            try {
                // TODO load data
            } catch (ResponseException e) {
                // TODO handle execution errors
            }
        });

        add(analyzeButton);
    }
}
