package gp6.harbor.harborapi.domain.pedido;

import com.fasterxml.jackson.annotation.JsonBackReference;
import gp6.harbor.harborapi.domain.produto.Produto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PedidoProdutoV2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_produto", nullable = false)
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    @JsonBackReference
    private PedidoV2 pedido;

    private Integer quantidade;

}