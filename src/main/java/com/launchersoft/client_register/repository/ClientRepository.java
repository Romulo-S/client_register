package com.launchersoft.client_register.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.launchersoft.client_register.model.Client;

public interface ClientRepository extends JpaRepository<Client,Long> {

    Page<Client> findAll(Pageable pageable);
    Page<Client> findByFirstNameContaining(String firstName, Pageable pageable);

    Client findByFirstNameContaining(String firstName);

    Page<Client> findByLastNameContaining(String firstName, Pageable pageable);

    Page<Client> findByEmailContaining(String firstName, Pageable pageable);



}
