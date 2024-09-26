package dev.skrock.camunda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class Camunda7ToolkitApplication {

    public static void main(String... args) {
        SpringApplicationBuilder appBuilder = new SpringApplicationBuilder()
                .sources(Camunda7ToolkitApplication.class)
                .web(WebApplicationType.NONE);

        appBuilder.run(args);
    }
}
