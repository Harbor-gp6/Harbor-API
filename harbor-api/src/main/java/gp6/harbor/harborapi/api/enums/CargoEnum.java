package gp6.harbor.harborapi.api.enums;

public enum CargoEnum {
    ADMIN("Admin"),
    EMPREGADO("Empregado"),
    ATENDENTE("Atendente");

    private String cargo;

    CargoEnum(String cargo) {
        this.cargo = cargo;
    }
}
