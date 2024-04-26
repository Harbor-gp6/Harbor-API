package gp6.harbor.harborapi.service.servico.dto;

import gp6.harbor.harborapi.domain.servico.Servico;

public class ServicoMapper {

    public static Servico toEntity(ServicoCriacaoDto dto) {
        Servico servico = new Servico();

        servico.setDescricaoServico(dto.getDescricaoServico());
        servico.setServicoEspecial(dto.getServicoEspecial());
        servico.setTempoMedioEmMinutos(dto.getTempoMedioEmMinutos());
        servico.setValorServico(dto.getValorServico());

        return servico;
    }
}
