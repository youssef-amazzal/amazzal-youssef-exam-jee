package amazzal.youssef.examjee.web;

import amazzal.youssef.examjee.dtos.CreditRequestDTO;
import amazzal.youssef.examjee.dtos.CreditResponseDTO;
import amazzal.youssef.examjee.dtos.RemboursementDTO;
import amazzal.youssef.examjee.entities.StatutCredit;
import amazzal.youssef.examjee.services.CreditService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/credits")
@AllArgsConstructor
@Slf4j

public class CreditRestController {

    private final CreditService creditService;

    @PostMapping("/demande")
    public ResponseEntity<CreditResponseDTO> demanderCredit(@RequestBody CreditRequestDTO creditRequestDTO) {
        log.info("POST /api/v1/credits/demande - Request to apply for a new credit for client ID: {}", creditRequestDTO.getClientId());
        CreditResponseDTO responseDTO = creditService.demanderCredit(creditRequestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{creditId}")
    public ResponseEntity<CreditResponseDTO> getCreditById(@PathVariable Long creditId) {
        log.info("GET /api/v1/credits/{} - Request to get credit by ID", creditId);
        CreditResponseDTO responseDTO = creditService.getCreditById(creditId);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<CreditResponseDTO>> getCreditsByClientId(@PathVariable Long clientId) {
        log.info("GET /api/v1/credits/client/{} - Request to get credits for client ID", clientId);
        List<CreditResponseDTO> credits = creditService.getCreditsByClientId(clientId);
        return ResponseEntity.ok(credits);
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<CreditResponseDTO>> getCreditsByStatut(@PathVariable String statut) {
        log.info("GET /api/v1/credits/statut/{} - Request to get credits by statut", statut);
        try {
            StatutCredit statutEnum = StatutCredit.valueOf(statut.toUpperCase());
            List<CreditResponseDTO> credits = creditService.getCreditsByStatut(statutEnum);
            return ResponseEntity.ok(credits);
        } catch (IllegalArgumentException e) {
            log.error("Invalid statut provided: {}", statut);
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{creditId}/approuver")
    public ResponseEntity<CreditResponseDTO> approuverCredit(@PathVariable Long creditId) {
        log.info("PATCH /api/v1/credits/{}/approuver - Request to approve credit", creditId);
        CreditResponseDTO responseDTO = creditService.approuverCredit(creditId);
        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping("/{creditId}/rejeter")
    public ResponseEntity<CreditResponseDTO> rejeterCredit(@PathVariable Long creditId) {
        log.info("PATCH /api/v1/credits/{}/rejeter - Request to reject credit", creditId);
        CreditResponseDTO responseDTO = creditService.rejeterCredit(creditId);
        return ResponseEntity.ok(responseDTO);
    }


    @PostMapping("/{creditId}/remboursements")
    public ResponseEntity<RemboursementDTO> ajouterRemboursement(@PathVariable Long creditId, @RequestBody RemboursementDTO remboursementDTO) {
        log.info("POST /api/v1/credits/{}/remboursements - Request to add remboursement", creditId);
        RemboursementDTO savedRemboursement = creditService.ajouterRemboursement(creditId, remboursementDTO);
        return new ResponseEntity<>(savedRemboursement, HttpStatus.CREATED);
    }

    @GetMapping("/{creditId}/remboursements")
    public ResponseEntity<List<RemboursementDTO>> getRemboursementsByCreditId(@PathVariable Long creditId) {
        log.info("GET /api/v1/credits/{}/remboursements - Request to get remboursements for credit ID", creditId);
        List<RemboursementDTO> remboursements = creditService.getRemboursementsByCreditId(creditId);
        return ResponseEntity.ok(remboursements);
    }
}