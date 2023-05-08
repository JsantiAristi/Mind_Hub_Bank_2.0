package com.mindhub.homebanking.services;

import com.mindhub.homebanking.Models.Client;
import com.mindhub.homebanking.dtos.ClientDTO;
import org.springframework.security.core.Authentication;
import java.util.List;

public interface ClientService {
    void saveClient(Client client);
    List<ClientDTO> getClientsDTO();
    ClientDTO getClientDTO(Authentication authentication);
    Client getClientAuthenticated(Authentication authentication);
    Client getClientWithEmail(String emailAddress);
}
