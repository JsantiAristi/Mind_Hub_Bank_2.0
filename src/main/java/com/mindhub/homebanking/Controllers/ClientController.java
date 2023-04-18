package com.mindhub.homebanking.Controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.repositories.ClientRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import static java.util.stream.Collectors.toList;

@RestController
public class ClientController {
    @Autowired
    private ClientRespository clientRespository;

    // Servlets
    @RequestMapping("/api/clients")
    public List<ClientDTO> getClients() {
        return clientRespository.findAll()
                .stream()
                .map(client -> new ClientDTO(client))
                .collect(toList());
    }

    @RequestMapping("/api/clients/{id}")
    public Optional<ClientDTO> getClient (@PathVariable Long id){
        return clientRespository.findById(id).map(client -> new ClientDTO(client));
    }

//    @PostMapping("/api/clients")
//    public void setClients(@RequestBody Client client){
//        clientRespository.findAll().add(client);
//    }
}
