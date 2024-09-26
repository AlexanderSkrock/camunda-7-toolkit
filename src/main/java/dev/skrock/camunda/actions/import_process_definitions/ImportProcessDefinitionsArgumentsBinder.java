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

package dev.skrock.camunda.actions.import_process_definitions;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import lombok.experimental.UtilityClass;

/**
 * <Beschreibung>
 * <br>
 *
 * @author askrock
 */
@UtilityClass
public class ImportProcessDefinitionsArgumentsBinder {

    private final String INPUT_PARAMETER_NAME = "input";

    public ImportProcessDefinitionArguments bind(Map<String, Object> args) {
        if (args == null) {
            return ImportProcessDefinitionArguments.defaultArgs();
        }

        AtomicReference<ImportProcessDefinitionArguments> exportArgs = new AtomicReference<>(ImportProcessDefinitionArguments.defaultArgs());

        bindInput(args).ifPresent(output -> exportArgs.getAndUpdate(current -> current.withInput(output)));

        return exportArgs.get();
    }
    private Optional<Path> bindInput(Map<String, Object> args) {
        if (!args.containsKey(INPUT_PARAMETER_NAME)) {
            return Optional.empty();
        }

        return Optional.ofNullable(args.get(INPUT_PARAMETER_NAME))
                       .filter(String.class::isInstance)
                       .map(String.class::cast)
                       .map(Paths::get);
    }
}
