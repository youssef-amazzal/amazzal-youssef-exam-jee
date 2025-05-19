package amazzal.youssef.examjee.mappers;

import amazzal.youssef.examjee.dtos.ClientDTO;
import amazzal.youssef.examjee.entities.Client;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ClientMapper {
    ClientDTO toClientDTO(Client client);
    Client toClient(ClientDTO clientDTO);
}