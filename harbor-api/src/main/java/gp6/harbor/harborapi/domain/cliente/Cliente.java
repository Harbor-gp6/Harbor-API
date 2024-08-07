package gp6.harbor.harborapi.domain.cliente;

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

    private String nome;

    private String sobrenome;

    private String email;

    private String telefone;

    private String cpf;
    @CreationTimestamp
    private LocalDate dataCriacao;
}
