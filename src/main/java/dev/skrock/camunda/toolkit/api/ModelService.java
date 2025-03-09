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

package dev.skrock.camunda.toolkit.api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.CallActivity;
import org.camunda.bpm.model.bpmn.instance.ServiceTask;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaField;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaIn;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaInputOutput;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaInputParameter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import dev.skrock.camunda.toolkit.model.CalledSapFunction;
import dev.skrock.camunda.toolkit.model.CalledSapTable;

/**
 * <Beschreibung>
 * <br>
 *
 * @author askrock
 */
@Service
public class ModelService {

    public Set<CalledSapFunction> analyzeSapCalls(BpmnModelInstance modelInstance) {
        MultiValueMap<CalledSapFunction, String> sapFunctionToActivities = new LinkedMultiValueMap<>();

        modelInstance.getModelElementsByType(ServiceTask.class).stream()
                .filter(serviceTask -> "${sapDelegate}".equals(serviceTask.getCamundaDelegateExpression()))
                .forEach(serviceTask -> {
                    CalledSapFunction sapFunction = createSapFunction(serviceTask);
                    sapFunctionToActivities.add(sapFunction, serviceTask.getId());
                });

        modelInstance.getModelElementsByType(CallActivity.class).stream()
                .filter(callActivity -> "RFC_READ_TABLEProcess".equals(callActivity.getCalledElement()))
                .forEach(callActivity -> {
                    CalledSapFunction sapFunction = createSapFunction(callActivity);
                    sapFunctionToActivities.add(sapFunction, callActivity.getId());
                });

        sapFunctionToActivities.forEach((sapFunction, activities) -> {
            sapFunction.setCalledFromActivityIds(new HashSet<>(activities));
        });
        return sapFunctionToActivities.keySet();
    }

    protected CalledSapFunction createSapFunction(ServiceTask serviceTask) {
        Map<String, CamundaField> fields = serviceTask.getExtensionElements().getElementsQuery().filterByType(CamundaField.class).list().stream().collect(Collectors.toMap(CamundaField::getCamundaName, Function.identity()));
        String systemName = fields.get("sapId").getCamundaExpressionChild().getTextContent();
        String fuba = fields.get("fuba").getCamundaExpressionChild().getTextContent();

        CamundaInputOutput inOut = serviceTask.getExtensionElements().getElementsQuery().filterByType(CamundaInputOutput.class).singleResult();
        Map<String, CamundaInputParameter> inputs = inOut.getCamundaInputParameters().stream().collect(Collectors.toMap(CamundaInputParameter::getCamundaName, Function.identity()));
        Map<String, String> inputValues = new HashMap<>();
        inputs.forEach((key, inputParameter) -> inputValues.put(key, inputParameter.getTextContent()));

        if (CalledSapTable.FUNCTION_NAME.equals(fuba)) {
            return new CalledSapTable(null, Set.of(serviceTask.getId()), systemName, inputValues);
        } else {
            return new CalledSapFunction(null, Set.of(serviceTask.getId()), systemName, fuba, inputValues);
        }
    }

    protected CalledSapFunction createSapFunction(CallActivity callActivity) {
        if ("RFC_READ_TABLEProcess".equals(callActivity.getCalledElement())) {
            AtomicReference<String> system = new AtomicReference<>();
            Map<String, String> inputValues = new HashMap<>();
            callActivity.getExtensionElements().getElementsQuery().filterByType(CamundaIn.class).list().forEach(camundaIn -> {
                String value = Optional.ofNullable(camundaIn.getCamundaSource()).orElseGet(camundaIn::getCamundaSourceExpression);
                if ("SAP_SYSTEM_ID".equals(camundaIn.getCamundaTarget())) {
                    system.set(value);
                } else {
                    inputValues.put(camundaIn.getCamundaTarget(), value);
                }
            });
            return new CalledSapTable(null, Set.of(callActivity.getId()), system.get(), inputValues);
        }

        throw new IllegalArgumentException("unsupported call activity: %s".formatted(callActivity.getCalledElement()));
    }
}
