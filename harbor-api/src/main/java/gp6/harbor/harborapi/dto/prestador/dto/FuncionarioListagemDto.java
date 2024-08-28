package gp6.harbor.harborapi.dto.prestador.dto;

import gp6.harbor.harborapi.api.enums.CargoEnum;
import gp6.harbor.harborapi.domain.prestador.AvaliacaoPrestador;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FuncionarioListagemDto {
    private String nome;
    private String sobrenome;
    private String telefone;
    private String cpf;
    private String email;
    private CargoEnum cargo;
    private double estrelas;
    private Integer qtdAvaliacoes;


}
