package amazzal.youssef.examjee.mappers;

import amazzal.youssef.examjee.dtos.RemboursementDTO;
import amazzal.youssef.examjee.entities.Remboursement;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RemboursementMapper {
    RemboursementDTO toRemboursementDTO(Remboursement remboursement);
    Remboursement toRemboursement(RemboursementDTO remboursementDTO);
}