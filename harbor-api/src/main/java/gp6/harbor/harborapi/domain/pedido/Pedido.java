package gp6.harbor.harborapi.domain.pedido;

import gp6.harbor.harborapi.domain.cliente.Cliente;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import gp6.harbor.harborapi.domain.produto.Produto;
import gp6.harbor.harborapi.domain.servico.Servico;
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

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Produto> listaProduto;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Servico> listaServico;

    @ManyToOne
    @JoinColumn(name = "id_prestador")
    private Prestador prestador;

    private LocalDateTime dataAgendamento;

    @CreationTimestamp
    private LocalDateTime dataCriacao;

    private String observacao;
}