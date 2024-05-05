package gp6.harbor.harborapi.domain.prestador;


import gp6.harbor.harborapi.domain.cargo.Cargo;
import gp6.harbor.harborapi.domain.empresa.Empresa;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Prestador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prestador")
    private Long id;
    private String nome;
    private String sobrenome;
    private String telefone;
    private String cpf;
    private String email;
    private String senha;
    @ManyToOne
    @JoinColumn(name = "id_empresa")
    private Empresa empresa;
    @ManyToOne
    @JoinColumn(name = "id_cargo")
    private Cargo cargo;

}