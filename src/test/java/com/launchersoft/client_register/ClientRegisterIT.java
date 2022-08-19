package com.launchersoft.client_register;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.Month;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.launchersoft.client_register.model.Client;
import com.launchersoft.client_register.repository.ClientRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ClientRegisterIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClientRepository clientRepository;

    @Test
    void getAllClients() throws Exception {

        Client client = new Client();
        client.setFirstName("Romulo");
        client.setLastName("Sorato");
        client.setEmail("romulosorato@hotmail.com");
        client.setDateOfBirth(LocalDate.of(1992, Month.JULY,20));

        mockMvc.perform(post("/api/v1/clients/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(client)))
            .andExpect(status().isOk());

        Client clientEntity = clientRepository.findByFirstNameContaining("Romulo");
        assertThat(clientEntity.getFirstName()).isEqualTo("Romulo");
        assertThat(clientEntity.getLastName()).isEqualTo("Sorato");
        assertThat(clientEntity.getEmail()).isEqualTo("romulosorato@hotmail.com");
        assertThat(clientEntity.getDateOfBirth()).isEqualTo(LocalDate.of(1992, Month.JULY,20));
    }

}
