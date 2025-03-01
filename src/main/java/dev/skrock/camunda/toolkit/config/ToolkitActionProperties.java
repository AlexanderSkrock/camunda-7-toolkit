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

package dev.skrock.camunda.toolkit.config;

import java.util.Map;

import dev.skrock.camunda.toolkit.actions.ToolkitActionType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <Beschreibung>
 * <br>
 *
 * @author askrock
 */
@Data
@Setter
@NoArgsConstructor
public class ToolkitActionProperties {

    ToolkitActionType actionType;

    Map<String, Object> args;
}
