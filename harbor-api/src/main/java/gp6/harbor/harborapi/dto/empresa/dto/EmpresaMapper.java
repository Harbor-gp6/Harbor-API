package gp6.harbor.harborapi.dto.empresa.dto;


import java.time.LocalDate;
import java.util.List;

import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.endereco.Endereco;
import gp6.harbor.harborapi.dto.endereco.dto.EnderecoMapper;
import gp6.harbor.harborapi.util.SlugUtils;
import jakarta.validation.Valid;

public class EmpresaMapper {

    public static Empresa toEntity(@Valid EmpresaCriacaoDto dto){
        if (dto == null){
            return null;
        }

        String slug = SlugUtils.createSlug(dto.getRazaoSocial());

        Empresa empresa = new Empresa();
        empresa.setRazaoSocial(dto.getRazaoSocial());
        empresa.setNomeFantasia(dto.getNomeFantasia());
        empresa.setSlug(slug);
        empresa.setCnpj(dto.getCnpj());
        empresa.setDataCriacao(LocalDate.now());
        empresa.setHorarioAbertura(dto.getHorarioAbertura());
        empresa.setHorarioFechamento(dto.getHorarioFechamento());

        Endereco endereco = EnderecoMapper.toEntity(dto.getEndereco());

        empresa.setEndereco(endereco);

        return empresa;
    }
    public static EmpresaListagemDto toDto(Empresa entity){
        if (entity == null) return null;

        EmpresaListagemDto listagemDto = new EmpresaListagemDto();
        listagemDto.setId(entity.getId());
        listagemDto.setFoto(entity.getFoto());
        listagemDto.setRazaoSocial(entity.getRazaoSocial());
        listagemDto.setNomeFantasia(entity.getNomeFantasia());
        listagemDto.setSlug(entity.getSlug());
        listagemDto.setEndereco(EnderecoMapper.toDto(entity.getEndereco()));
        listagemDto.setCnpj(entity.getCnpj());
        return listagemDto;
    }

    public static List<EmpresaListagemDto> toDto(List<Empresa> entities){
        return entities.stream().map(EmpresaMapper::toDto).toList();
    }

}
