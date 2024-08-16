package gp6.harbor.harborapi.dto.empresa.dto;

import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import gp6.harbor.harborapi.dto.prestador.dto.PrestadorFuncionarioCriacao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmpresaMapperStruct {
    EmpresaCriacaoDto toDto(Empresa prestador);
    EmpresaListagemDto toDtoListagem(Empresa prestador);

    @Mapping(target = "id", ignore = true)  // Ignora o campo id
    @Mapping(target = "dataInativacao", ignore = true)  // Ignora o campo horariosOcupados
    Empresa toEntity(EmpresaCriacaoDto prestadorDto);
}
