package gp6.harbor.harborapi.dto.prestador.dto;

import gp6.harbor.harborapi.domain.prestador.Prestador;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PrestadorMapperStruct {

    // Mapeia de Prestador para PrestadorFuncionarioCriacao
    PrestadorFuncionarioCriacao toDto(Prestador prestador);

    FuncionarioListagemDto toFuncionarioListagemDto(Prestador prestador);


    // Mapeia de PrestadorFuncionarioCriacao para Prestador
    @Mapping(target = "id", ignore = true)  // Ignora o campo id
    @Mapping(target = "empresa", ignore = true)  // Ignora o campo empresa
    @Mapping(target = "foto", ignore = true)  // Ignora o campo foto
    @Mapping(target = "horariosOcupados", ignore = true)  // Ignora o campo horariosOcupados
    Prestador toEntity(PrestadorFuncionarioCriacao prestadorDto);
}
