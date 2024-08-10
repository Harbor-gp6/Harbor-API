package gp6.harbor.harborapi.domain.pedido;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import gp6.harbor.harborapi.api.enums.FormaPagamentoEnum;
import gp6.harbor.harborapi.domain.cliente.Cliente;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import gp6.harbor.harborapi.domain.produto.Produto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pedido")
    @JsonManagedReference
    private List<PedidoPrestador> pedidoPrestador;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pedido")
    @JsonManagedReference
    private List<PedidoProdutoV2> pedidoProdutos;

    @CreationTimestamp
    private LocalDateTime dataCriacao;

    private Boolean finalizado = false  ;

    private LocalDateTime dataAgendamento;

    @Enumerated(EnumType.STRING)
    private FormaPagamentoEnum formaPagamentoEnum;


}