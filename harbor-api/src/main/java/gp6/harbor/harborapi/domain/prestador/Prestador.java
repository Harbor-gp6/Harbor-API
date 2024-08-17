package gp6.harbor.harborapi.domain.prestador;


import com.fasterxml.jackson.annotation.JsonIgnore;
import gp6.harbor.harborapi.api.enums.CargoEnum;
import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.pedido.repository.HorarioOcupado;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@Entity
public class Prestador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prestador")
    private Long id;
    private String nome;
    private String sobrenome;
    private String telefone;
    private String cpf;
    @JsonIgnore
    @Column(length = 50 * 1024 * 1024)
    private byte[] foto;
    private String email;
    private String senha;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_empresa")
    private Empresa empresa;
    @Enumerated(EnumType.STRING)
    private CargoEnum cargo;

    @OneToMany(mappedBy = "prestador", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<HorarioOcupado> horariosOcupados = new ArrayList<>();

    public boolean adicionarHorarioOcupado(HorarioOcupado horarioOcupado) {
        for (HorarioOcupado ocupado : this.horariosOcupados) {
            if (colide(ocupado, horarioOcupado)) {
                // Existe uma colisão de horários
                return false;
            }
        }
        // Se não houver colisão, adiciona o novo horário à lista de horários ocupados
        this.horariosOcupados.add(horarioOcupado);
        return true;
    }

    private boolean colide(HorarioOcupado ocupado, HorarioOcupado novo) {
        return novo.getDataInicio().isBefore(ocupado.getDataFim()) &&
                novo.getDataFim().isAfter(ocupado.getDataInicio());
    }
}