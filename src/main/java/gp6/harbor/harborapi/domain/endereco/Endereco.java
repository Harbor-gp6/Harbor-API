package gp6.harbor.harborapi.domain.endereco;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_endereco")
    private Integer id;

    private String bairro;
    private String logradouro;
    private String cidade;
    private String estado;
    private String numero;
    private String cep;
    private String complemento;
}
