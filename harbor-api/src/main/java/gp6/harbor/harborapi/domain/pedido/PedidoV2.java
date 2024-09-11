package gp6.harbor.harborapi.domain.pedido;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import gp6.harbor.harborapi.api.enums.FormaPagamentoEnum;
import gp6.harbor.harborapi.api.enums.StatusPedidoEnum;
import gp6.harbor.harborapi.domain.cliente.Cliente;
import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import gp6.harbor.harborapi.domain.produto.Produto;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
public class PedidoV2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Integer id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_empresa")
    private Empresa empresa;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pedido")
    @JsonManagedReference
    private List<PedidoPrestador> pedidoPrestador;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pedido")
    @JsonManagedReference
    private List<PedidoProdutoV2> pedidoProdutos;

    @CreationTimestamp
    private LocalDateTime dataCriacao;

    @Enumerated(EnumType.STRING)
    private StatusPedidoEnum statusPedidoEnum = StatusPedidoEnum.ABERTO;

    @FutureOrPresent
    private LocalDateTime dataAgendamento;

    @Enumerated(EnumType.STRING)
    private FormaPagamentoEnum formaPagamentoEnum;

    private UUID codigoPedido;

    private Double totalPedido;

    public void calcularTotalPedido() {
        this.totalPedido = 0.0;
        for (PedidoProdutoV2 pedidoProduto : pedidoProdutos) {
            this.totalPedido += pedidoProduto.getProduto().getPrecoVenda();
        }
        for (PedidoPrestador pedidoPrestador : pedidoPrestador) {
            this.totalPedido += pedidoPrestador.getServico().getValorServico();
        }
    }


}