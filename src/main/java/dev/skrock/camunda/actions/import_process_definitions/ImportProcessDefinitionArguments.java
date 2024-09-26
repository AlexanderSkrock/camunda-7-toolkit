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

package dev.skrock.camunda.actions.import_process_definitions;

import java.nio.file.Path;
import java.nio.file.Paths;

import lombok.Value;
import lombok.With;

/**
 * <Beschreibung>
 * <br>
 *
 * @author askrock
 */
@Value
public class ImportProcessDefinitionArguments {

    public static ImportProcessDefinitionArguments defaultArgs() {
        return new ImportProcessDefinitionArguments(null);
    }

    @With
    Path input;
}
