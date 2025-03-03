package dev.skrock.camunda.toolkit.engine;

import lombok.Data;

@Data
public class RemoteCamundaEngine {
    private String name;
    private String url;
    private String username;
    private String password;
}
