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

package dev.skrock.camunda.toolkit.actions.analyze_variable_sizes;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.util.unit.DataSize;

import lombok.Value;
import lombok.With;

/**
 * <Beschreibung>
 * <br>
 *
 * @author askrock
 */
@Value
public class AnalyzeVariableSizesArguments {

    public static AnalyzeVariableSizesArguments defaultArgs() {
        return new AnalyzeVariableSizesArguments(Paths.get("").toAbsolutePath(), DataSize.ofKilobytes(1), null);
    }

    @With
    Path output;

    @With
    DataSize minSizeLimit;

    @With
    String processInstanceId;
}
