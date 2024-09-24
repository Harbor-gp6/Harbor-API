package gp6.harbor.harborapi.dto.prestador.dto;

import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import gp6.harbor.harborapi.dto.empresa.dto.EmpresaMapper;

import java.util.List;

public class PrestadorMapper {
    public static Prestador toEntity(PrestadorCriacaoDto dto){
        if (dto == null){
            return null;
        }
        Prestador prestador = new Prestador();

        if (!(dto.getEmpresa() == null)) {
            Empresa empresa = EmpresaMapper.toEntity(dto.getEmpresa());
            prestador.setEmpresa(empresa);
        }

        prestador.setNome(dto.getNome());
        prestador.setSobrenome(dto.getSobrenome());
        prestador.setTelefone(dto.getTelefone());
        prestador.setCpf(dto.getCpf());
        prestador.setEmail(dto.getEmail());
        prestador.setSenha(dto.getSenha());
        prestador.setFoto(dto.getFoto());

        return prestador;
    }

    public static PrestadorListagemDto toDto(Prestador entity){
        if (entity == null) return null;

        PrestadorListagemDto listagemDto = new PrestadorListagemDto();
        listagemDto.setId(Math.toIntExact(entity.getId()));
        listagemDto.setEmpresa(EmpresaMapper.toDto(entity.getEmpresa()));
        listagemDto.setNome(entity.getNome());
        listagemDto.setSobrenome(entity.getSobrenome());
        listagemDto.setTelefone(entity.getTelefone());
        listagemDto.setCpf(entity.getCpf());
        listagemDto.setEmail(entity.getEmail());
        listagemDto.setFoto(entity.getFoto());
        listagemDto.setCargo(entity.getCargo());

        return listagemDto;
    }
    public static List<PrestadorListagemDto> toDto(List<Prestador> entities){
        return entities.stream().map(PrestadorMapper::toDto).toList();
    }

}