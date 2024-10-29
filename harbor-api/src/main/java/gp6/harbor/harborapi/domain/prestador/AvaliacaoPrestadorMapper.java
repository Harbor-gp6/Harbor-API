package gp6.harbor.harborapi.domain.prestador;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AvaliacaoPrestadorMapper {

    @Mapping(source = "prestador.nome", target = "nomePrestador")
    @Mapping(source = "cliente.nome", target = "nomeCliente")
    @Mapping(source = "cliente.id", target = "idCliente")
    AvaliacaoPrestadorDto toDto(AvaliacaoPrestador avaliacaoPrestador);

    List<AvaliacaoPrestadorDto> toDtoList(List<AvaliacaoPrestador> avaliacaoPrestadores);

    @InheritInverseConfiguration
    AvaliacaoPrestador toEntity(AvaliacaoPrestadorDto avaliacaoPrestadorDto);
}