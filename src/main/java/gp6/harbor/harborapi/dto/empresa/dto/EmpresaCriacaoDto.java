package gp6.harbor.harborapi.dto.empresa.dto;

import gp6.harbor.harborapi.domain.endereco.Endereco;
import gp6.harbor.harborapi.dto.endereco.dto.EnderecoCriacaoDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToOne;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CNPJ;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class EmpresaCriacaoDto {

    @NotBlank(message = "A razão social não pode estar em branco")
    @Size(min = 2, max = 300, message = "A razão social deve ter entre 2 e 300 caracteres")
    private String razaoSocial;

    @Size(max = 255)
    private String nomeFantasia;

    @CNPJ
    private String cnpj;

    @NotNull
    private EnderecoCriacaoDto endereco;

    @NotNull
    private LocalTime horarioAbertura;

    @NotNull
    private LocalTime horarioFechamento;

}