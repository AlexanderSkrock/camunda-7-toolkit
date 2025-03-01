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

package dev.skrock.camunda.toolkit.actions;

import java.util.Map;

import dev.skrock.camunda.toolkit.actions.errors.ExecutionException;

/**
 * <Beschreibung>
 * <br>
 *
 * @author askrock
 */
public interface ToolkitActionExecutor {

    ToolkitActionType getActionType();

    void execute(Map<String, Object> args) throws ExecutionException;
}
