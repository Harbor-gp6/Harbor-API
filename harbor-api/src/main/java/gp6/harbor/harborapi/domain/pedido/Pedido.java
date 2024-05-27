package gp6.harbor.harborapi.domain.pedido;

import gp6.harbor.harborapi.api.enums.FormaPagamento;
import gp6.harbor.harborapi.domain.cliente.Cliente;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Integer id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_prestador")
    private Prestador prestador;

    private LocalDateTime dataAgendamento;

    @OneToMany
    private List<PedidoProduto> pedidoProdutos;

    @OneToMany
    private List<PedidoServico> pedidoServicos;

    @CreationTimestamp
    private LocalDateTime dataCriacao;

    private Boolean finalizado;

    private Double total;

    private FormaPagamento formaPagamento;
}
