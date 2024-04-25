package gp6.harbor.harborapi.service.cargo.dto;

public class CargoListagemDto {
    private Integer id;
    private String nomeCargo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomeCargo() {
        return nomeCargo;
    }

    public void setNomeCargo(String nomeCargo) {
        this.nomeCargo = nomeCargo;
    }
}
