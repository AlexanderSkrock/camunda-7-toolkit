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

package dev.skrock.camunda.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

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
@ConfigurationProperties(prefix = "toolkit")
public class ToolkitProperties {

    List<ToolkitActionProperties> actions;
}
