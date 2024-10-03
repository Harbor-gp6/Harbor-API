package gp6.harbor.harborapi.domain.foto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gp6.harbor.harborapi.domain.empresa.Empresa;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Foto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento com Empresa
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @Lob
    private byte[] dados;
}