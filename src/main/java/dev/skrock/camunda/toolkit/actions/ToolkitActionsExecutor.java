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

package dev.skrock.camunda.toolkit.actions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.skrock.camunda.toolkit.actions.errors.ExecutionException;
import dev.skrock.camunda.toolkit.config.ToolkitProperties;
import lombok.extern.slf4j.Slf4j;

/**
 * <Beschreibung>
 * <br>
 *
 * @author askrock
 */
@Slf4j
@Component
public class ToolkitActionsExecutor {

    private final ToolkitProperties toolkitProperties;

    private final List<ToolkitActionExecutor> actionExecutors;

    @Autowired
    public ToolkitActionsExecutor(ToolkitProperties toolkitProperties, List<ToolkitActionExecutor> actionExecutors) {
        this.toolkitProperties = toolkitProperties;
        this.actionExecutors = actionExecutors;
    }

    public void executeActions() {
        if (toolkitProperties.getActions().isEmpty()) {
            log.warn("no actions configured, skipping execution");
            return;
        }

        List<ExecutionStep> steps = toolkitProperties.getActions().stream().map(action -> {
            ToolkitActionExecutor executor = actionExecutors
                    .stream()
                    .filter(e -> e.getActionType() == action.getActionType())
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("no executor available for action %s".formatted(action.getActionType())));

            return new ExecutionStep(executor, action.getArgs());
        }).toList();

        log.info("discovered {} steps", steps.size());

        for (int i = 0; i < steps.size(); i++) {
            ExecutionStep currentStep = steps.get(i);
            log.info("executing step {} of {}:\n\t{}", i + 1, steps.size(), currentStep);
            try {
                currentStep.execute();
            } catch (ExecutionException e) {
                log.error("failed to execute step", e);
                log.error("aborting execution");
                break;
            }
            log.info("finished step {} of {}", i + 1, steps.size());
        }
    }
}
