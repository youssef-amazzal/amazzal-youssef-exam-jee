package amazzal.youssef.examjee.dtos;

import amazzal.youssef.examjee.entities.StatutCredit;
import amazzal.youssef.examjee.entities.TypeBienFinance;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;
import java.util.List; 

@Data @NoArgsConstructor @AllArgsConstructor
public class CreditResponseDTO {
    private Long id;
    private Date dateDemande;
    private StatutCredit statut;
    private Date dateAcceptation;
    private double montant;
    private int dureeRemboursement;
    private double tauxInteret;

    private ClientDTO client;

    private String typeCreditDiscriminator; 
    private String motif; 
    private TypeBienFinance typeBienFinance; 
    private String raisonSocialeEntreprise; 
    private List<RemboursementDTO> remboursements;
}