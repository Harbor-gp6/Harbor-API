package gp6.harbor.harborapi.dto.prestador.dto;

import gp6.harbor.harborapi.domain.pedido.HorarioOcupadoDTO;
import gp6.harbor.harborapi.domain.pedido.repository.HorarioOcupado;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HorarioOcupadoMapper {
    //detalhar conversao do id

    HorarioOcupadoDTO toDto(HorarioOcupado horarioOcupado);
    HorarioOcupado toEntity(HorarioOcupadoDTO horarioPrestadorDto);
}
