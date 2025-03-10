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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CalledDbQuery {

    @EqualsAndHashCode.Include
    private String callingProcessDefinitionId;

    private Set<String> calledFromActivityIds = new HashSet<>();

    @EqualsAndHashCode.Include
    private String system;

    @EqualsAndHashCode.Include
    private String sql;

    @EqualsAndHashCode.Include
    private Map<String, String> inputs;
}
