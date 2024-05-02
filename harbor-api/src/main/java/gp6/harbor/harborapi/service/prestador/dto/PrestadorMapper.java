package gp6.harbor.harborapi.service.prestador.dto;

import gp6.harbor.harborapi.domain.cargo.Cargo;
import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import gp6.harbor.harborapi.service.cargo.dto.CargoMapper;
import gp6.harbor.harborapi.service.empresa.dto.EmpresaMapper;

import java.util.List;

public class PrestadorMapper {
    public static Prestador toEntity(PrestadorCriacaoDto dto){
        if (dto == null){
            return null;
        }
        Prestador prestador = new Prestador();

        Empresa empresa = dto.getEmpresa();
        prestador.setEmpresa(empresa);

        prestador.setNome(dto.getNome());
        prestador.setSobrenome(dto.getSobrenome());
        prestador.setTelefone(dto.getTelefone());
        prestador.setCpf(dto.getCpf());
        prestador.setEmail(dto.getEmail());
        prestador.setSenha(dto.getSenha());


        Cargo cargo = dto.getCargo();
        prestador.setCargo(cargo);

        return prestador;
    }

    public static PrestadorListagemDto toDto(Prestador entity){
        if (entity == null) return null;

        PrestadorListagemDto listagemDto = new PrestadorListagemDto();
        listagemDto.setId(Math.toIntExact(entity.getId()));
        listagemDto.setEmpresa(entity.getEmpresa());
        listagemDto.setNome(entity.getNome());
        listagemDto.setSobrenome(entity.getSobrenome());
        listagemDto.setTelefone(entity.getTelefone());
        listagemDto.setCpf(entity.getCpf());
        listagemDto.setEmail(entity.getEmail());
        listagemDto.setCargo(entity.getCargo());

        return listagemDto;
    }
    public static List<PrestadorListagemDto> toDto(List<Prestador> entities){
        return entities.stream().map(PrestadorMapper::toDto).toList();
    }
}