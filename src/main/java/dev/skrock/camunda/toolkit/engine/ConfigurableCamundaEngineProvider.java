package dev.skrock.camunda.toolkit.engine;

public class ConfigurableCamundaEngineProvider implements CamundaEngineProvider {

    private RemoteCamundaEngine engine;

    @Override
    public RemoteCamundaEngine provide() {
        ensureEngine();
        return engine;
    }

    public void configureEngine(RemoteCamundaEngine engine) {
        this.engine = engine;
    }

    protected void ensureEngine() {
        if (engine == null) {
            throw new IllegalStateException("no active camunda engine");
        }
    }
}
