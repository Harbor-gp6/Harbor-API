package gp6.harbor.harborapi.dto.prestador.dto;

import gp6.harbor.harborapi.api.enums.CargoEnum;
import gp6.harbor.harborapi.dto.empresa.dto.EmpresaListagemDto;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class PrestadorListagemDto {
    private Integer id;
    private EmpresaListagemDto empresa;
    private String foto;
    private String nome;
    private String sobrenome;
    private String telefone;
    private String cpf;
    private String email;
    private CargoEnum cargo;

    public void setFoto(byte[] foto) {
        this.foto = truncateBase64(foto);
    }

    private String truncateBase64(byte[] foto) {
        if (foto == null) {
            return null;
        }
        String base64 = java.util.Base64.getEncoder().encodeToString(foto);
        return base64.length() > 20 ? base64.substring(0, 20) + "..." : base64;
    }
}
