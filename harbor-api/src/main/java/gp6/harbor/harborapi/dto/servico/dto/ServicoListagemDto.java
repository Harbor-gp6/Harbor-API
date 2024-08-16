package gp6.harbor.harborapi.dto.servico.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServicoListagemDto {

    private Integer id;
    private String foto;
    private String descricaoServico;
    private Boolean servicoEspecial;
    private Integer tempoMedioEmMinutos;
    private Double valorServico;

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
