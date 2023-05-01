package com.mindhub.homebanking.Controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.ClientLoanDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
public class LoanController {

//    @RequestMapping("/api/loans")
//    public List<ClientLoanDTO> getClients() {
//        return clientRespository.findAll().stream().map(client -> new ClientDTO(client)).collect(toList());
//    }
}
