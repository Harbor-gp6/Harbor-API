package gp6.harbor.harborapi.service.empresa.dto;

import gp6.harbor.harborapi.domain.endereco.Endereco;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Getter
@Setter
public class EmpresaListagemDto {

    private String razaoSocial;

    private String nomeFantasia;

    private String cnpj;

    private Endereco endereco;

    private LocalDate dataCriacao;

    private LocalDate dataInativacao;

}
