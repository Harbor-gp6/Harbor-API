package gp6.harbor.harborapi.domain.prestador;

import gp6.harbor.harborapi.domain.cargo.Cargo;
import gp6.harbor.harborapi.domain.empresa.Empresa;
import jakarta.persistence.*;
import jdk.jfr.Enabled;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Prestador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prestador")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "id_empresa")
    private Empresa empresa;
    private String nome;
    private String sobrenome;
    private String telefone;
    private String cpf;
    private String email;
    private String senha;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_cargo")
    private Cargo cargo;
}
