package amazzal.youssef.examjee.mappers;

import amazzal.youssef.examjee.dtos.CreditResponseDTO;
import amazzal.youssef.examjee.entities.*;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ClientMapper.class, RemboursementMapper.class})
public interface CreditMapper {

    @Mapping(source = "client", target = "client") 
    @Mapping(source = "remboursements", target = "remboursements")
    @Mapping(target = "typeCreditDiscriminator", expression = "java(credit.getClass().getAnnotation(jakarta.persistence.DiscriminatorValue.class) != null ? credit.getClass().getAnnotation(jakarta.persistence.DiscriminatorValue.class).value() : null)")
    CreditResponseDTO toCreditResponseDTO(Credit credit);

    List<CreditResponseDTO> toCreditResponseDTOs(List<Credit> credits);

    @AfterMapping
    default void mapSpecificCreditFields(@MappingTarget CreditResponseDTO dto, Credit source) {
        if (source instanceof CreditPersonnel personnel) {
            dto.setMotif(personnel.getMotif());
        } else if (source instanceof CreditImmobilier immobilier) {
            dto.setTypeBienFinance(immobilier.getTypeBienFinance());
        } else if (source instanceof CreditProfessionnel professionnel) {
            dto.setMotif(professionnel.getMotif());
            dto.setRaisonSocialeEntreprise(professionnel.getRaisonSocialeEntreprise());
        }
    }
}