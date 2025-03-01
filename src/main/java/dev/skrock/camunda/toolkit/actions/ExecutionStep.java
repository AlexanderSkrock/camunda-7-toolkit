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
import lombok.AllArgsConstructor;

/**
 * <Beschreibung>
 * <br>
 *
 * @author askrock
 */
@AllArgsConstructor
public class ExecutionStep {

    ToolkitActionExecutor executor;

    Map<String, Object> args;

    public void execute() throws ExecutionException {
        executor.execute(args);
    }

    public String toString() {
         return """
                 Action: %s
                 Executor: %s
                 Args: %s
                 """.formatted(executor.getActionType(), executor.getClass(), args);
    }
}
