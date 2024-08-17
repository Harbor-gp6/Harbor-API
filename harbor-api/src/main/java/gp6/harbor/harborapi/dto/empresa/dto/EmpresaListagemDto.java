package gp6.harbor.harborapi.dto.empresa.dto;

import gp6.harbor.harborapi.domain.endereco.Endereco;
import gp6.harbor.harborapi.dto.endereco.dto.EnderecoListagemDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmpresaListagemDto {
    private Integer id;
    private String foto;
    private String razaoSocial;
    private String nomeFantasia;
    private String cnpj;
    private EnderecoListagemDto endereco;


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
