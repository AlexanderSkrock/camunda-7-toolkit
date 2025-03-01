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

package dev.skrock.camunda.toolkit.actions.export;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import lombok.experimental.UtilityClass;

/**
 * <Beschreibung>
 * <br>
 *
 * @author askrock
 */
@UtilityClass
public class ExportArgumentsBinder {

    private final String OUTPUT_PARAMETER_NAME = "output";
    private final String INCLUDE_PARAMETER_NAME = "include";

    public ExportArguments bind(Map<String, Object> args) {
        if (args == null) {
            return ExportArguments.defaultArgs();
        }

        AtomicReference<ExportArguments> exportArgs = new AtomicReference<>(ExportArguments.defaultArgs());

        bindOutput(args).ifPresent(output -> exportArgs.getAndUpdate(current -> current.withOutput(output)));
        bindIncludes(args).ifPresent(includes -> exportArgs.getAndUpdate(current -> current.withIncludes(includes)));

        return exportArgs.get();
    }
    private Optional<Path> bindOutput(Map<String, Object> args) {
        if (!args.containsKey(OUTPUT_PARAMETER_NAME)) {
            return Optional.empty();
        }

        return Optional.ofNullable(args.get(OUTPUT_PARAMETER_NAME))
                       .filter(String.class::isInstance)
                       .map(String.class::cast)
                       .map(Paths::get);
    }

    private Optional<Set<Includes>> bindIncludes(Map<String, Object> args) {
        if (!args.containsKey(INCLUDE_PARAMETER_NAME)) {
            return Optional.empty();
        }

        Optional<Collection> includeNames = Optional
                .ofNullable(args.get(INCLUDE_PARAMETER_NAME))
                .filter(Collection.class::isInstance)
                .map(Collection.class::cast)
                .or(() -> Optional.ofNullable(args.get(INCLUDE_PARAMETER_NAME))
                                  .filter(Map.class::isInstance)
                                  .map(Map.class::cast)
                                  .map(Map::values));

        return includeNames.map(names -> {
            if (names.contains("process-definition")) {
                return Set.of(Includes.PROCESS_DEFINITION);
            }
            return Collections.emptySet();
        });
    }
}
