package gp6.harbor.harborapi.dto.empresa.dto;


import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.endereco.Endereco;
import gp6.harbor.harborapi.dto.endereco.dto.EnderecoMapper;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

public class EmpresaMapper {

    public static Empresa toEntity(@Valid EmpresaCriacaoDto dto){
        if (dto == null){
            return null;
        }

        Empresa empresa = new Empresa();
        empresa.setRazaoSocial(dto.getRazaoSocial());
        empresa.setNomeFantasia(dto.getNomeFantasia());
        empresa.setCnpj(dto.getCnpj());
        empresa.setDataCriacao(LocalDate.now());

        Endereco endereco = EnderecoMapper.toEntity(dto.getEndereco());

        empresa.setEndereco(endereco);

        return empresa;
    }
    public static EmpresaListagemDto toDto(Empresa entity){
        if (entity == null) return null;

        EmpresaListagemDto listagemDto = new EmpresaListagemDto();
        listagemDto.setRazaoSocial(entity.getRazaoSocial());
        listagemDto.setNomeFantasia(entity.getNomeFantasia());
        listagemDto.setEndereco(entity.getEndereco());
        listagemDto.setCnpj(entity.getCnpj());
        return listagemDto;
    }

    public static List<EmpresaListagemDto> toDto(List<Empresa> entities){
        return entities.stream().map(EmpresaMapper::toDto).toList();
    }

}
