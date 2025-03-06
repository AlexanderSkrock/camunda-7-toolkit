package dev.skrock.camunda.toolkit;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import dev.skrock.camunda.toolkit.util.SSLUtil;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;

@SpringBootApplication(
        // Our ace editor is based on a gson version that is not compatible with spring boot
        exclude = GsonAutoConfiguration.class
)
public class Camunda7ToolkitApplication {

    public static void main(String... args) throws NoSuchAlgorithmException, KeyManagementException {
        SSLUtil.turnOffSslChecking();
        SpringApplication.run(Camunda7ToolkitApplication.class);
    }
}
