package dev.skrock.camunda;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import dev.skrock.camunda.util.SSLUtil;

@SpringBootApplication
public class Camunda7ToolkitApplication {

    public static void main(String... args) throws NoSuchAlgorithmException, KeyManagementException {
        SSLUtil.turnOffSslChecking();
        SpringApplication.run(Camunda7ToolkitApplication.class);
    }
}
