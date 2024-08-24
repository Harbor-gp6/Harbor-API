package gp6.harbor.harborapi.domain.cliente;

import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.interfaces.IEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Cliente implements IEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Integer id;

    @NotBlank
    private String nome;

    @NotBlank
    private String sobrenome;

    @NotBlank
    private String email;

    @NotBlank
    private String telefone;

    @NotBlank
    @CPF
    private String cpf;

    @CreationTimestamp
    private LocalDate dataCriacao;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_empresa")
    private Empresa empresa;
}
