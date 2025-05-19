package amazzal.youssef.examjee.services.impl;

import amazzal.youssef.examjee.dtos.ClientDTO;
import amazzal.youssef.examjee.entities.Client;
import amazzal.youssef.examjee.mappers.ClientMapper;
import amazzal.youssef.examjee.repositories.ClientRepository;
import amazzal.youssef.examjee.services.ClientService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Override
    public ClientDTO saveClient(ClientDTO clientDTO) {
        log.info("Saving new Client: {}", clientDTO.getNom());
        Client client = clientMapper.toClient(clientDTO);
        Client savedClient = clientRepository.save(client);
        return clientMapper.toClientDTO(savedClient);
    }

    @Override
    public ClientDTO getClient(Long clientId) {
        log.info("Fetching client with id: {}", clientId);
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new EntityNotFoundException("Client with id " + clientId + " not found"));
        return clientMapper.toClientDTO(client);
    }

    @Override
    public List<ClientDTO> getAllClients() {
        log.info("Fetching all clients");
        List<Client> clients = clientRepository.findAll();
        return clients.stream().map(clientMapper::toClientDTO).collect(Collectors.toList());
    }

    @Override
    public void deleteClient(Long clientId) {
        log.warn("Deleting client with id: {}", clientId);

        if (!clientRepository.existsById(clientId)) {
            throw new EntityNotFoundException("Client with id " + clientId + " not found, cannot delete.");
        }
        clientRepository.deleteById(clientId);
    }
}