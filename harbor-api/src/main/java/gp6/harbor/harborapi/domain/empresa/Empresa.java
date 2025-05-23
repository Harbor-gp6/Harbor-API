package gp6.harbor.harborapi.domain.empresa;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    @Column(length = 50 * 1024 * 1024)
    private String foto;
    @JsonIgnore
    @Column(length = 50 * 1024 * 1024)
    private String banner;
    private String razaoSocial;
    private String nomeFantasia;
    private String slug;
    private String cnpj;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_endereco")
    private Endereco endereco;
    @CreationTimestamp
    private LocalDate dataCriacao = LocalDate.now();
    private LocalTime horarioAbertura;
    private LocalTime horarioFechamento;
    private LocalDate dataInativacao;

}
