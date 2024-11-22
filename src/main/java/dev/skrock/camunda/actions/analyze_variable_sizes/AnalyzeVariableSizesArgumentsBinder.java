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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.util.unit.DataSize;

import dev.skrock.camunda.actions.export.Includes;
import lombok.experimental.UtilityClass;

/**
 * <Beschreibung>
 * <br>
 *
 * @author askrock
 */
@UtilityClass
public class AnalyzeVariableSizesArgumentsBinder {

    private final String OUTPUT_PARAMETER_NAME = "output";
    private final String MIN_SIZE_LIMIT_PARAMETER_NAME = "minSizeLimit";

    private final String PROCESS_INSTANCE_ID_PARAMETER_NAME = "processInstanceId";

    public AnalyzeVariableSizesArguments bind(Map<String, Object> args) {
        AtomicReference<AnalyzeVariableSizesArguments> analyzeArgs = new AtomicReference<>(AnalyzeVariableSizesArguments.defaultArgs());

        bindOutput(args).ifPresent(output -> analyzeArgs.getAndUpdate(current -> current.withOutput(output)));
        bindMinSizeLimit(args).ifPresent(minSizeLimit -> analyzeArgs.getAndUpdate(current -> current.withMinSizeLimit(minSizeLimit)));

        String processInstanceId = bindProcessInstanceId(args).orElseThrow();
        analyzeArgs.getAndUpdate(current -> current.withProcessInstanceId(processInstanceId));

        return analyzeArgs.get();
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

    private Optional<DataSize> bindMinSizeLimit(Map<String, Object> args) {
        if (!args.containsKey(MIN_SIZE_LIMIT_PARAMETER_NAME)) {
            return Optional.empty();
        }
        return Optional.ofNullable(args.get(MIN_SIZE_LIMIT_PARAMETER_NAME))
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .map(DataSize::parse);

    }

    private Optional<String> bindProcessInstanceId(Map<String, Object> args) {
        if (!args.containsKey(PROCESS_INSTANCE_ID_PARAMETER_NAME)) {
            return Optional.empty();
        }

        return Optional.ofNullable(args.get(PROCESS_INSTANCE_ID_PARAMETER_NAME))
                       .filter(String.class::isInstance)
                       .map(String.class::cast);
    }
}
