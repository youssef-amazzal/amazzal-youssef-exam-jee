package amazzal.youssef.examjee.dtos;

import amazzal.youssef.examjee.entities.TypeRemboursement;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;

@Data @NoArgsConstructor @AllArgsConstructor
public class RemboursementDTO {
    private Long id;
    private Date dateRemboursement;
    private double montant;
    private TypeRemboursement type;
}