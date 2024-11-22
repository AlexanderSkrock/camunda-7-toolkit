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

package dev.skrock.camunda.actions.analyze_variable_sizes;

import java.nio.charset.StandardCharsets;

import org.camunda.community.rest.client.model.HistoricVariableInstanceDto;
import org.springframework.util.unit.DataSize;

import lombok.Data;
import lombok.experimental.UtilityClass;

/**
 * <Beschreibung>
 * <br>
 *
 * @author askrock
 */
@UtilityClass
public class VariablesSizes {

    public static DataSize getVariableSize(HistoricVariableInstanceDto variable) {
        if (variable == null || variable.getValue() == null) {
            return DataSize.ofBytes(0);
        }

        return DataSize.ofBytes(variable.getValue().toString().getBytes(StandardCharsets.UTF_8).length);
    }
}
