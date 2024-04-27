package gp6.harbor.harborapi.domain.produto;

import gp6.harbor.harborapi.domain.empresa.Empresa;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produto")
    private Integer id;

    private String nome;

    @ManyToOne
    @JoinColumn(name = "id_empresa")
    private Empresa empresa;

    private String descricao;

    private Double precoCompra;

    private Double precoVenda;
}
