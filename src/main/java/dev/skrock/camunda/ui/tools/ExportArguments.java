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

package dev.skrock.camunda.ui.tools;

import java.util.HashSet;
import java.util.Set;

import dev.skrock.camunda.actions.export.Includes;
import lombok.Data;

/**
 * <Beschreibung>
 * <br>
 *
 * @author askrock
 */
@Data
public class ExportArguments {

    private Set<Includes> includes = new HashSet<>();
}
