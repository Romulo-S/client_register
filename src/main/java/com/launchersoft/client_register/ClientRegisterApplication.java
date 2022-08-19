package com.launchersoft.client_register;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Clients API", version = "1.0", description = "Clients Information"))
public class ClientRegisterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientRegisterApplication.class, args);
    }

}
