package com.launchersoft.client_register.configuration;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import com.launchersoft.client_register.controller.ClientController;

@Component
public class RestResourceMainConfig extends ResourceConfig {
    public RestResourceMainConfig() {
        System.out.println("Registering Rest Configuration...");

        register(ClientController.class);
        register(CORSResponseFilter.class);
    }
}
