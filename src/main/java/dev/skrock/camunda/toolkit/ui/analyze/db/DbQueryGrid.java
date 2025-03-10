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

import com.vaadin.flow.component.grid.Grid;
import dev.skrock.camunda.toolkit.model.CalledDbQuery;

/**
 * <Beschreibung>
 * <br>
 *
 * @author askrock
 */
public class DbQueryGrid extends Grid<CalledDbQuery> {

    public DbQueryGrid() {
        super(CalledDbQuery.class, false);
        addColumn(CalledDbQuery::getSystem).setHeader("System").setAutoWidth(true).setFlexGrow(0).setSortable(true);
        addColumn(CalledDbQuery::getSql).setHeader("SQL").setSortable(true);
        addColumn(CalledDbQuery::getInputs).setHeader("Inputs");
    }
}
