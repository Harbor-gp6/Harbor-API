package gp6.harbor.harborapi.service.cargo.dto;

import jakarta.validation.constraints.NotBlank;

public class CargoCriacaoDto {

    @NotBlank(message = "O nome do cargo não pode estar em branco")
    private String nomeCargo;

    public String getNomeCargo() {
        return nomeCargo;
    }

    public void setNomeCargo(String nomeCargo) {
        this.nomeCargo = nomeCargo;
    }
}
