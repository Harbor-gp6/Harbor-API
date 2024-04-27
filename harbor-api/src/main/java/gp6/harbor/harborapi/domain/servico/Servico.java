package gp6.harbor.harborapi.domain.servico;

import gp6.harbor.harborapi.domain.empresa.Empresa;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Servico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String descricaoServico;
    private Boolean servicoEspecial;
    private Integer tempoMedioEmMinutos;
    private Double valorServico;
    @ManyToOne
    @JoinColumn(name = "id_empresa")
    private Empresa empresa;

}
