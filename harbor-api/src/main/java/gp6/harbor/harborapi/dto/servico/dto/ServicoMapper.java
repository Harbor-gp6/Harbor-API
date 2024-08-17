package gp6.harbor.harborapi.dto.servico.dto;

import gp6.harbor.harborapi.domain.servico.Servico;
import jakarta.validation.Valid;

import java.util.List;

public class ServicoMapper {
    public static Servico toEntity(ServicoCriacaoDto dto){
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

    public static Servico toEntity(ServicoAtualizacaoDto dto) {
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
        listagemDto.setId(entity.getId());
        listagemDto.setDescricaoServico(entity.getDescricaoServico());
        listagemDto.setServicoEspecial(entity.getServicoEspecial());
        listagemDto.setTempoMedioEmMinutos(entity.getTempoMedioEmMinutos());
        listagemDto.setValorServico(entity.getValorServico());
        listagemDto.setFoto(entity.getFoto());

        return listagemDto;
    }
    public static List<ServicoListagemDto> toDto(List<Servico> entities){
        return entities.stream().map(ServicoMapper::toDto).toList();
    }
}