package gp6.harbor.harborapi.service.servico.dto;


import gp6.harbor.harborapi.domain.empresa.Empresa;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServicoListagemDto {

    private String descricaoServico;
    private Boolean servicoEspecial;
    private Integer tempoMedioEmMinutos;
    private Double valorServico;

}
