package dev.skrock.camunda;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import dev.skrock.camunda.util.SSLUtil;

@SpringBootApplication
public class Camunda7ToolkitApplication {

    public static void main(String... args) throws NoSuchAlgorithmException, KeyManagementException {
        SSLUtil.turnOffSslChecking();

        SpringApplicationBuilder appBuilder = new SpringApplicationBuilder()
                .sources(Camunda7ToolkitApplication.class)
                .web(WebApplicationType.NONE);

        appBuilder.run(args);
    }
}
