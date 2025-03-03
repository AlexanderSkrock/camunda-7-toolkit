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

package dev.skrock.camunda.toolkit.ui.tools.analyze.variables;

import lombok.Data;
import org.springframework.util.unit.DataSize;

/**
 * <Beschreibung>
 * <br>
 *
 * @author askrock
 */
@Data
public class AnalyzeVariablesArguments {

    private DataSize minSize = DataSize.ofBytes(0);

    private String processInstanceId;
}
