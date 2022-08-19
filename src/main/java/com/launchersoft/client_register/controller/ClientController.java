package com.launchersoft.client_register.controller;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.launchersoft.client_register.exception.ResourceNotFoundException;
import com.launchersoft.client_register.model.Client;
import com.launchersoft.client_register.repository.ClientRepository;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllClients(@RequestParam(required = false) String firstName,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "3") int size) {

        try {
            Pageable paging = PageRequest.of(page, size);
            Page<Client> pageClients = null;

            if (firstName == null) {
                pageClients = clientRepository.findAll(paging);
            } else {
                pageClients = clientRepository.findByFirstNameContaining(firstName, paging);
            }

            return getMapResponseEntity(pageClients);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void setAge(List<Client> content) {
        for (Client client : content) {
            LocalDate dateOfBirth = client.getDateOfBirth();
            if(dateOfBirth != null){
                int age = calculateAge(dateOfBirth);
                client.setAge(age);
            }
        }
    }

    private int calculateAge(LocalDate birthday) {
        LocalDate today = LocalDate.now();

        Period period = Period.between(birthday, today);

        return period.getYears();
    }

    @GetMapping("/clients/email")
    public ResponseEntity<Map<String, Object>> getClientsByEmail(@RequestParam(required = true) String email,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "3") int size) {

        try {
            Pageable paging = PageRequest.of(page, size);
            Page<Client> pageClients = null;

            pageClients = clientRepository.findByEmailContaining(email, paging);

            return getMapResponseEntity(pageClients);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<Map<String, Object>> getMapResponseEntity(Page<Client> pageClients) {
        Map<String, Object> response = new HashMap<>();

        setAge(pageClients.getContent());

        response.put("clients", pageClients.getContent());
        response.put("currentPage", pageClients.getNumber());
        response.put("totalItems", pageClients.getTotalElements());
        response.put("totalPages", pageClients.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/clients/lastName")
    public ResponseEntity<Map<String, Object>> getClientsByLastName(@RequestParam(required = true) String lastName,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "3") int size) {

        try {
            Pageable paging = PageRequest.of(page, size);
            Page<Client> pageClients = null;

            pageClients = clientRepository.findByFirstNameContaining(lastName, paging);

            return getMapResponseEntity(pageClients);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("{id}")
    public ResponseEntity<Client> getClientById(@PathVariable long id) {
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Client id not exist:" + id));

        setAge(client);
        return ResponseEntity.ok(client);
    }

    private void setAge(Client client) {
        LocalDate dateOfBirth = client.getDateOfBirth();
        if(dateOfBirth != null){
            int age = calculateAge(dateOfBirth);
            client.setAge(age);
        }
    }

    @PostMapping
    public Client createClient(@RequestBody Client client) {
        return clientRepository.save(client);
    }

    @PutMapping("{id}")
    public ResponseEntity<Client> updateEmployee(@PathVariable long id, @RequestBody Client clientUpdated) {
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Client not exist with id: " + id));

        client.setFirstName(clientUpdated.getFirstName());
        client.setLastName(clientUpdated.getLastName());
        client.setEmail(clientUpdated.getEmail());

        clientRepository.save(client);

        return ResponseEntity.ok(client);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> deleteClient(@PathVariable long id) {

        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Client not exist with id: " + id));

        clientRepository.delete(client);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }
}
