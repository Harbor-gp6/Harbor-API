package gp6.harbor.harborapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Empresa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "A razão social não pode estar em branco")
    @Size(min = 2, max = 300, message = "A razão social deve ter entre 2 e 300 caracteres")
    private String razaoSocial;
}
