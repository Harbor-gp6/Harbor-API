package gp6.harbor.harborapi.domain.pedido;

import com.fasterxml.jackson.annotation.JsonBackReference;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import gp6.harbor.harborapi.domain.servico.Servico;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PedidoPrestador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_prestador", nullable = false)
    private Prestador prestador;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_servico", nullable = false)
    private Servico servico;

    @ManyToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    @JsonBackReference
    private PedidoV2 pedido;
}