package gp6.harbor.harborapi.domain.empresa;

import gp6.harbor.harborapi.domain.endereco.Endereco;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.br.CNPJ;


import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empresa")
    private Integer id;
    private String razaoSocial;

    private String nomeFantasia;

    private String cnpj;

    private boolean endereco;

    @CreationTimestamp
    private LocalDate dataCriacao = LocalDate.now();

    private LocalTime horarioAbertura;

    private LocalTime horarioFechamento;

    private LocalDate dataInativacao;

}
