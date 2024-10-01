package gp6.harbor.harborapi.api.controller.atividadePedido;

import gp6.harbor.harborapi.domain.AtividadePedido.AtividadePedido;
import gp6.harbor.harborapi.service.AtividadePedido.AtividadePedidoService;
import gp6.harbor.harborapi.service.prestador.AvaliacaoPrestadorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/atividades-pedido")
@RequiredArgsConstructor
public class AtividadePedidoController {
    private final AtividadePedidoService atividadePedidoService;

    @GetMapping("/{codigoPedido}")
    @SecurityRequirement(name = "Bearer")
    public AtividadePedido obterAtividadesPedido(@PathVariable UUID codigoPedido) {
        return atividadePedidoService.buscarAtividadePedido(codigoPedido);
    }
    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public List<AtividadePedido> obterAtividadesPedido() {
        return atividadePedidoService.listarAtividadesPedido();
    }

    @GetMapping("/atividade-pedido-por-prestador")
    @SecurityRequirement(name = "Bearer")
    public AtividadePedido obterAtividadesPedidoPorPrestador() {
        return atividadePedidoService.atividadePedidoPorUsuarioLogado();
    }

}
