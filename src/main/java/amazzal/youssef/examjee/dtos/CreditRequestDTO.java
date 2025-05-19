package amazzal.youssef.examjee.dtos;

import amazzal.youssef.examjee.entities.TypeBienFinance;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class CreditRequestDTO {
    private Double montant;
    private Integer dureeRemboursement; 
    private Long clientId; 
    private String creditType;
    private String motif;
    private TypeBienFinance typeBienFinance;
    private String raisonSocialeEntreprise;
}