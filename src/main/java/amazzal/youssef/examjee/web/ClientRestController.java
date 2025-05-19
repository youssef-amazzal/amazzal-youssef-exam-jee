package amazzal.youssef.examjee.web;

import amazzal.youssef.examjee.dtos.ClientDTO;
import amazzal.youssef.examjee.services.ClientService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
@AllArgsConstructor
@Slf4j
public class ClientRestController {

    private final ClientService clientService;

    @GetMapping
    public ResponseEntity<List<ClientDTO>> getAllClients() {
        log.info("GET /api/v1/clients - Request to get all clients");
        List<ClientDTO> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable Long clientId) {
        log.info("GET /api/v1/clients/{} - Request to get client by ID", clientId);
        ClientDTO clientDTO = clientService.getClient(clientId);
        return ResponseEntity.ok(clientDTO);


    }

    @PostMapping
    public ResponseEntity<ClientDTO> saveClient(@RequestBody ClientDTO clientDTO) {

        log.info("POST /api/v1/clients - Request to save new client: {}", clientDTO.getNom());
        ClientDTO savedClient = clientService.saveClient(clientDTO);
        return new ResponseEntity<>(savedClient, HttpStatus.CREATED);
    }

    @DeleteMapping("/{clientId}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long clientId) {
        log.info("DELETE /api/v1/clients/{} - Request to delete client by ID", clientId);
        clientService.deleteClient(clientId);
        return ResponseEntity.noContent().build();
    }

}