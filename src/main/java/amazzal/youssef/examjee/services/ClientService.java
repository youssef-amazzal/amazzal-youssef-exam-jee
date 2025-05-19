package amazzal.youssef.examjee.services;

import amazzal.youssef.examjee.dtos.ClientDTO;

import java.util.List;

public interface ClientService {
    ClientDTO saveClient(ClientDTO clientDTO);

    ClientDTO getClient(Long clientId);

    List<ClientDTO> getAllClients();

    void deleteClient(Long clientId);
}