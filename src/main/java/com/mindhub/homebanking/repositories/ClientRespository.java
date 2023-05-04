package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.Models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ClientRespository extends JpaRepository<Client , Long>{
    Client findByEmailAddress(String emailAddress);
}
