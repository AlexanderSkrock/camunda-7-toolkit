package dev.skrock.camunda.toolkit.config;

import dev.skrock.camunda.toolkit.engine.RemoteCamundaEngine;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "toolkit")
public class ToolkitProperties {

    private List<RemoteCamundaEngine> engines = new LinkedList<>();
}
