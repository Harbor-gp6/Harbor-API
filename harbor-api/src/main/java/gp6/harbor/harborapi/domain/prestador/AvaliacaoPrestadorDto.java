package gp6.harbor.harborapi.domain.prestador;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AvaliacaoPrestadorDto {
    private Integer id;
    private String nomePrestador;
    private String nomeCliente;
    private UUID codigoPedido;
    private Double estrelas;
    private String comentario;
    private String cnpjEmpresa;
    private Long idCliente;  // Novo campo para o ID do cliente
}
