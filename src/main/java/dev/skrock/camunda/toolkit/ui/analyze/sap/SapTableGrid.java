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
import dev.skrock.camunda.toolkit.model.CalledSapTable;

/**
 * <Beschreibung>
 * <br>
 *
 * @author askrock
 */
public class SapTableGrid extends Grid<CalledSapTable> {

    public SapTableGrid() {
        super(CalledSapTable.class, false);
        addColumn(CalledSapFunction::getSystem).setHeader("System");
        addColumn(CalledSapTable::getTable).setHeader("Table");
        addColumn(CalledSapTable::getOptions).setHeader("Options");
        addColumn(CalledSapTable::getInputs).setHeader("Inputs");
    }
}
