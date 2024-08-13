package gp6.harbor.harborapi.domain.pedido.repository;

import gp6.harbor.harborapi.domain.prestador.Prestador;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


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

    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;

}