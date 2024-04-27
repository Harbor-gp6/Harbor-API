package gp6.harbor.harborapi.service.servico.dto;

import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.servico.Servico;
import gp6.harbor.harborapi.service.empresa.dto.EmpresaMapper;
import jakarta.validation.Valid;

import java.util.List;

public class ServicoMapper {
    public static Servico toEntity(@Valid ServicoCriacaoDto dto){
        if (dto == null){
            return null;
        }

        Servico servico = new Servico();

        servico.setDescricaoServico(dto.getDescricaoServico());
        servico.setServicoEspecial(dto.getServicoEspecial());
        servico.setTempoMedioEmMinutos(dto.getTempoMedioEmMinutos());
        servico.setValorServico(dto.getValorServico());


        return servico;
    }
    public static ServicoListagemDto toDto(Servico entity){
        if (entity == null) return null;

        ServicoListagemDto listagemDto = new ServicoListagemDto();
        listagemDto.setDescricaoServico(entity.getDescricaoServico());
        listagemDto.setServicoEspecial(entity.getServicoEspecial());
        listagemDto.setTempoMedioEmMinutos(entity.getTempoMedioEmMinutos());
        listagemDto.setValorServico(entity.getValorServico());

        return listagemDto;
    }
    public static List<ServicoListagemDto> toDto(List<Servico> entities){
        return entities.stream().map(ServicoMapper::toDto).toList();
    }
}