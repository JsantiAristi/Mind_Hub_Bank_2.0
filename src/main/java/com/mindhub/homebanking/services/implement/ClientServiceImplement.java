package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.Models.Client;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.repositories.ClientRespository;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.List;
import static java.util.stream.Collectors.toList;

@Service
public class ClientServiceImplement implements ClientService {
    @Autowired
    ClientRespository clientRespository;

    @Override
    public void saveClient(Client client) { clientRespository.save(client);}

    @Override
    public List<ClientDTO> getClientsDTO() {
        return clientRespository.findAll().stream().map(client -> new ClientDTO(client)).collect(toList());}

    @Override
    public ClientDTO getClientDTO(Authentication authentication) {
        return new ClientDTO(clientRespository.findByEmailAddress(authentication.getName()));}

    @Override
    public Client getClientAuthenticated(Authentication authentication) {
        return clientRespository.findByEmailAddress(authentication.getName());}

    @Override
    public Client getClientWithEmail(String emailAddress) {
        return clientRespository.findByEmailAddress(emailAddress);
    }

}
