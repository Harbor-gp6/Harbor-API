package gp6.harbor.harborapi.dto.servico.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServicoListagemDto {

    private Integer id;
    private String descricaoServico;
    private Boolean servicoEspecial;
    private Integer tempoMedioEmMinutos;
    private Double valorServico;

}
