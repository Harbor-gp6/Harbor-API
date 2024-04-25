package gp6.harbor.harborapi.domain.servico;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Servico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "A descrição do serviço não pode estar em branco")
    @Size(min = 2, max = 45, message = "A descrição do serviço deve ter entre 2 e 45 caracteres")
    private String descricaoServico;
    private Boolean servicoEspecial;
}
