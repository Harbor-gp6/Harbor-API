package gp6.harbor.harborapi.domain.pedido.repository;

import gp6.harbor.harborapi.domain.pedido.PedidoV2;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;


@Setter
@Getter
@Entity
@Table(name = "horario_ocupado")
public class HorarioOcupado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_horario_ocupado")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_prestador")
    private Prestador prestador;

    //adiciona o pedido que esse hrário pertence
    @ManyToOne
    @JoinColumn(name = "id_pedido")
    private PedidoV2 pedido;

    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;

    private UUID codigoPedido;

}