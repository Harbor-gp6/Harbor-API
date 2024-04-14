package gp6.harbor.harborapi.dto;

import gp6.harbor.harborapi.entity.Prestador;

import java.util.List;

public class PrestadorMapper {
    public static Prestador toEntity(PrestadorCriacaoDto dto){
        if (dto == null){
            return null;
        }
        Prestador prestador = new Prestador();
        prestador.setEmpresa(dto.getEmpresa());
        prestador.setNome(dto.getNome());
        prestador.setSobrenome(dto.getSobrenome());
        prestador.setTelefone(dto.getTelefone());
        prestador.setCpf(dto.getCpf());
        prestador.setEmail(dto.getEmail());
        prestador.setSenha(dto.getSenha());
        prestador.setCargo(dto.getCargo());

        return prestador;
    }

    public static PrestadorListagemDto toDto(Prestador entity){
        if (entity == null) return null;

        PrestadorListagemDto listagemDto = new PrestadorListagemDto();
        listagemDto.setId(entity.getId());
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
