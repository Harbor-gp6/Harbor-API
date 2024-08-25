package gp6.harbor.harborapi.api.controller.pedido;

import gp6.harbor.harborapi.domain.pedido.Pedido;
import gp6.harbor.harborapi.domain.pedido.PedidoV2;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import gp6.harbor.harborapi.dto.pedido.dto.PedidoAtualizacaoProdutoDto;
import gp6.harbor.harborapi.dto.pedido.dto.PedidoCriacaoDto;
import gp6.harbor.harborapi.dto.pedido.dto.PedidoListagemDto;
import gp6.harbor.harborapi.dto.pedido.dto.PedidoMapper;
import gp6.harbor.harborapi.exception.PedidoCapacidadeExcedidoException;
import gp6.harbor.harborapi.dto.pedido.dto.*;
import gp6.harbor.harborapi.service.cliente.ClienteService;
import gp6.harbor.harborapi.service.pedido.PedidoService;
import gp6.harbor.harborapi.service.prestador.PrestadorService;
import gp6.harbor.harborapi.util.PedidoFilaEspera;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    public static PedidoFilaEspera<Pedido> filaPedido = new PedidoFilaEspera<>(10);
    private final PedidoService pedidoService;
    private final PrestadorService prestadorService;

    @PostMapping("criarPedidoV2")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<PedidoV2CriacaoDto> criarPedido(@RequestBody PedidoV2CriacaoDto pedido) {
        pedidoService.criarPedidoV2(pedido);
        return ResponseEntity.ok(pedido);
    }

    @Hidden
    @PostMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<PedidoListagemDto> criarPedido(@RequestBody @Valid PedidoCriacaoDto novoPedido) {
        Prestador prestador = prestadorService.buscarPorId(novoPedido.getPrestadorId());

        Pedido pedido = PedidoMapper.toEntity(novoPedido);

        pedido.setPrestador(prestador);

        try {
            Pedido pedidoSalvo = pedidoService.criarPedido(pedido, novoPedido.getServicos());
            PedidoListagemDto listagemDto = PedidoMapper.toDto(pedidoSalvo);
            return ResponseEntity.status(201).body(listagemDto);

        } catch (PedidoCapacidadeExcedidoException e) {
            return ResponseEntity.status(429).build();
        }
    }

    @Hidden
    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<PedidoListagemDto>> listarPedidos() {
        List<Pedido> pedidos = pedidoService.listarPedidos();

        if (pedidos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(200).body(PedidoMapper.toDto(pedidos));
    }

    @GetMapping("/pedidosV2")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<PedidoV2>> listarPedidosV2() {
        List<PedidoV2> pedidos = pedidoService.listarPedidosV2();

        if (pedidos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(200).body(pedidos);
    }

    @Hidden
    @GetMapping("/prestador/{prestadorId}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<PedidoListagemDto>> listarPorNomePrestador(@PathVariable Long prestadorId) {
        List<Pedido> pedidos = pedidoService.listarPorPrestador(prestadorId);

        if (pedidos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().body(PedidoMapper.toDto(pedidos));
    }

    @Hidden
    @PatchMapping("/produtos/{pedidoId}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<PedidoListagemDto> adicionarProduto(@PathVariable Integer pedidoId, @Valid @RequestBody PedidoAtualizacaoProdutoDto produtos) {
        return ResponseEntity.ok(PedidoMapper.toDto(pedidoService.adicionarProduto(pedidoId, produtos)));
    }

    @Hidden
    @PatchMapping("/status/{pedidoId}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<PedidoListagemDto> atualizarStatus(@PathVariable Integer pedidoId, @Valid @RequestBody PedidoAtualizacaoStatusDto status) {
       Pedido pedidoAtualizado = pedidoService.atualizarStatus(pedidoId, status);

       return ResponseEntity.ok(PedidoMapper.toDto(pedidoAtualizado));
    }

}
