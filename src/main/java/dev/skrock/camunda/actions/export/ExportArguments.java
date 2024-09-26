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

package dev.skrock.camunda.actions.export;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Set;

import lombok.Value;
import lombok.With;

/**
 * <Beschreibung>
 * <br>
 *
 * @author askrock
 */
@Value
public class ExportArguments {

    public static ExportArguments defaultArgs() {
        return new ExportArguments(Paths.get("").toAbsolutePath(), Collections.emptySet());
    }

    @With
    Path output;

    @With
    Set<Includes> includes;
}
