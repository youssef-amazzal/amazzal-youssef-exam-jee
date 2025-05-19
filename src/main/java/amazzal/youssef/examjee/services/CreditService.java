package amazzal.youssef.examjee.services;

import amazzal.youssef.examjee.dtos.CreditRequestDTO;
import amazzal.youssef.examjee.dtos.CreditResponseDTO;
import amazzal.youssef.examjee.dtos.RemboursementDTO;
import amazzal.youssef.examjee.entities.StatutCredit;

import java.util.List;

public interface CreditService {
    CreditResponseDTO demanderCredit(CreditRequestDTO creditRequestDTO);

    CreditResponseDTO getCreditById(Long creditId);

    List<CreditResponseDTO> getCreditsByClientId(Long clientId);

    List<CreditResponseDTO> getCreditsByStatut(StatutCredit statut);

    CreditResponseDTO approuverCredit(Long creditId);

    CreditResponseDTO rejeterCredit(Long creditId);

    RemboursementDTO ajouterRemboursement(Long creditId, RemboursementDTO remboursementDTO);

    List<RemboursementDTO> getRemboursementsByCreditId(Long creditId);
}