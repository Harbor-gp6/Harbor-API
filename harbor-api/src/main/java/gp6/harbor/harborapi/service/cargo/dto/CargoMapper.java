package gp6.harbor.harborapi.service.cargo.dto;


import gp6.harbor.harborapi.domain.cargo.Cargo;

import jakarta.validation.Valid;

import java.util.List;

public class CargoMapper {
    public static Cargo toEntity(@Valid CargoCriacaoDto CargoDto){
        if (CargoDto == null){
            return null;
        }

        Cargo cargo = new Cargo();

        cargo.setNomeCargo(CargoDto.getNomeCargo());
        
        return cargo;
    }

    public static CargoListagemDto toDto(Cargo entity){
        if (entity == null) return null;

        CargoListagemDto listagemDto = new CargoListagemDto();
        listagemDto.setId(entity.getId());
        listagemDto.setNomeCargo(entity.getNomeCargo());

        return listagemDto;

    }

    public static List<CargoListagemDto> toDto(List<Cargo> entities){
        return entities.stream().map(CargoMapper::toDto).toList();
    }
}
