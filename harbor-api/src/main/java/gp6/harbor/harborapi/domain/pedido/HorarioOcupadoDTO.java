package gp6.harbor.harborapi.domain.pedido;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class HorarioOcupadoDTO {

    @Id
    @Column(name = "id_horario_ocupado")
    private Long id;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
}
