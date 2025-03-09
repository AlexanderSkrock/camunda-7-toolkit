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

package dev.skrock.camunda.toolkit.model;

import java.util.Map;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <Beschreibung>
 * <br>
 *
 * @author askrock
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class CalledSapTable extends CalledSapFunction {

    public static final String FUNCTION_NAME = "RFC_READ_TABLE";
    public static final String TABLE_PARAMETER_NAME = "QUERY_TABLE";
    public static final String OPTIONS_PARAMETER_NAME = "OPTIONS";

    public CalledSapTable(String callingProcessDefinitionId, Set<String> calledFromActivityIds, String system, Map<String, String> inputs) {
        super(callingProcessDefinitionId, calledFromActivityIds, system, FUNCTION_NAME, inputs);
    }

    @Override
    public void setFunctionName(String functionName) {
        super.setFunctionName(FUNCTION_NAME);
    }

    public String getTable() {
        // TODO Extract parameter name from this class
        return getInputs().get(TABLE_PARAMETER_NAME);
    }

    public String getOptions() {
        // TODO Extract parameter name from this class
        return getInputs().get(OPTIONS_PARAMETER_NAME);
    }
}
