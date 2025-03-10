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

import com.vaadin.flow.component.grid.Grid;
import dev.skrock.camunda.toolkit.model.CalledSapFunction;

/**
 * <Beschreibung>
 * <br>
 *
 * @author askrock
 */
public class SapFunctionGrid extends Grid<CalledSapFunction> {

    public SapFunctionGrid() {
        super(CalledSapFunction.class, false);
        addColumn(CalledSapFunction::getSystem).setHeader("System");
        addColumn(CalledSapFunction::getFunctionName).setHeader("Function");
        addColumn(CalledSapFunction::getInputs).setHeader("Inputs");
    }
}
