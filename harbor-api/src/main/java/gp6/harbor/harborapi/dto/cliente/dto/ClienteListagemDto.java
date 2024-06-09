package gp6.harbor.harborapi.dto.cliente.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class ClienteListagemDto {

    private Integer id;

    private String nome;

    private String sobrenome;

    private String email;

    private String telefone;

    private String cpf;

    private LocalDate dataCriacao;
}
