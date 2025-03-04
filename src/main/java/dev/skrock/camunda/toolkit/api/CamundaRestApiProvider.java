package dev.skrock.camunda.toolkit.api;

import dev.skrock.camunda.toolkit.rest.RestApiMethod;
import dev.skrock.camunda.toolkit.rest.SwaggerRestApiMethod;
import org.camunda.community.rest.client.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
public class CamundaRestApiProvider {

    private final Map<Class<?>, Object> apiToImplementation = new HashMap<>();

    @Autowired
    public CamundaRestApiProvider(
            AuthorizationApi authorizationApi,
            BatchApi batchApi,
            ConditionApi conditionApi,
            DecisionDefinitionApi decisionDefinitionApi,
            DecisionRequirementsDefinitionApi decisionRequirementsDefinitionApi,
            DeploymentApi deploymentApi,
            EngineApi engineApi,
            EventSubscriptionApi eventSubscriptionApi,
            ExecutionApi executionApi,
            ExternalTaskApi externalTaskApi,
            FilterApi filterApi,
            GroupApi groupApi,
            HistoryApi historyApi,
            IdentityApi identityApi,
            IncidentApi incidentApi,
            JobApi jobApi,
            JobDefinitionApi jobDefinitionApi,
            MessageApi messageApi,
            MetricsApi metricsApi,
            MigrationApi migrationApi,
            ModificationApi modificationApi,
            ProcessDefinitionApi processDefinitionApi,
            ProcessInstanceApi processInstanceApi,
            SchemaApi schemaApi,
            SignalApi signalApi,
            TaskApi taskApi,
            TelemetryApi telemetryApi,
            TenantApi tenantApi,
            UserApi userApi,
            VariableInstanceApi variableInstanceApi,
            VersionApi versionApi
    ) {
        apiToImplementation.put(AuthorizationApi.class, authorizationApi);
        apiToImplementation.put(BatchApi.class, batchApi);
        apiToImplementation.put(ConditionApi.class, conditionApi);
        apiToImplementation.put(DecisionDefinitionApi.class, decisionDefinitionApi);
        apiToImplementation.put(DecisionRequirementsDefinitionApi.class, decisionRequirementsDefinitionApi);
        apiToImplementation.put(DeploymentApi.class, deploymentApi);
        apiToImplementation.put(EngineApi.class, engineApi);
        apiToImplementation.put(EventSubscriptionApi.class, eventSubscriptionApi);
        apiToImplementation.put(ExecutionApi.class, executionApi);
        apiToImplementation.put(ExternalTaskApi.class, externalTaskApi);
        apiToImplementation.put(FilterApi.class, filterApi);
        apiToImplementation.put(GroupApi.class, groupApi);
        apiToImplementation.put(HistoryApi.class, historyApi);
        apiToImplementation.put(IdentityApi.class, identityApi);
        apiToImplementation.put(IncidentApi.class, incidentApi);
        apiToImplementation.put(JobApi.class, jobApi);
        apiToImplementation.put(JobDefinitionApi.class, jobDefinitionApi);
        apiToImplementation.put(MessageApi.class, messageApi);
        apiToImplementation.put(MetricsApi.class, metricsApi);
        apiToImplementation.put(MigrationApi.class, migrationApi);
        apiToImplementation.put(ModificationApi.class, modificationApi);
        apiToImplementation.put(ProcessDefinitionApi.class, processDefinitionApi);
        apiToImplementation.put(ProcessInstanceApi.class, processInstanceApi);
        apiToImplementation.put(SchemaApi.class, schemaApi);
        apiToImplementation.put(SignalApi.class, signalApi);
        apiToImplementation.put(TaskApi.class, taskApi);
        apiToImplementation.put(TelemetryApi.class, telemetryApi);
        apiToImplementation.put(TenantApi.class, tenantApi);
        apiToImplementation.put(UserApi.class, userApi);
        apiToImplementation.put(VariableInstanceApi.class, variableInstanceApi);
        apiToImplementation.put(VersionApi.class, versionApi);
    }

    public <T> T getApiImplementation(RestApiMethod apiMethod) {
        Class<?> type = apiMethod.declaringType();
        return (T) apiToImplementation.get(type);
    }

    public MultiValueMap<Class<?>, RestApiMethod> getApiMethods() {
        MultiValueMap<Class<?>, RestApiMethod> apiMethods = new LinkedMultiValueMap<>();

        apiToImplementation.forEach((interfaceClazz, implementation) -> {
            for (Method m : interfaceClazz.getMethods()) {
                if (m.isAnnotationPresent(RequestMapping.class)) {
                    SwaggerRestApiMethod method = new SwaggerRestApiMethod(m);
                    apiMethods.add(interfaceClazz, method);
                }
            }
        });

        return apiMethods;
    }
}
