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

import org.camunda.community.rest.client.model.HistoricVariableInstanceDto;
import org.springframework.util.unit.DataSize;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Value;
import lombok.With;

/**
 * <Beschreibung>
 * <br>
 *
 * @author askrock
 */
@Value
public class VariableSizeInfo {

    public static VariableSizeInfo of(HistoricVariableInstanceDto variable) {
        return new VariableSizeInfo(variable.getId(), variable.getName(), VariablesSizes.getVariableSize(variable));
    }

    @With
    private String variableInstanceId;

    @With
    private String variableName;

    @With
    @JsonSerialize(using = ToStringSerializer.class)
    private DataSize variableSize;
}
