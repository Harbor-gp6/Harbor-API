package gp6.harbor.harborapi.api.controller.pedido;

import gp6.harbor.harborapi.domain.pedido.Pedido;
import gp6.harbor.harborapi.domain.pedido.PedidoV2;
import gp6.harbor.harborapi.domain.pedido.PedidoV2DTO;
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
import java.util.UUID;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    public static PedidoFilaEspera<Pedido> filaPedido = new PedidoFilaEspera<>(10);
    private final PedidoService pedidoService;
    private final PrestadorService prestadorService;

    @PostMapping("criarPedidoV2")
    public ResponseEntity<PedidoV2CriacaoDto> criarPedido(@RequestBody PedidoV2CriacaoDto pedido) {
        pedidoService.criarPedidoV2(pedido);
        return ResponseEntity.ok(pedido);
    }

    @PatchMapping("atualizarPedidoV2")
    public ResponseEntity<PedidoV2CriacaoDto> atualizarPedidoV2(@RequestBody PedidoV2CriacaoDto pedido, @RequestParam Integer idPedido) {
        pedidoService.atualizarPedidoV2(idPedido, pedido);
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
    @GetMapping("/pedidos")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<PedidoListagemDto>> listarPedidos() {
        List<Pedido> pedidos = pedidoService.listarPedidos();

        if (pedidos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(200).body(PedidoMapper.toDto(pedidos));
    }

    @GetMapping("/pedidosAbertos")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<PedidoV2DTO>> listarPedidosAbertos() {
        // Chama o serviço para listar os pedidos abertos
        List<PedidoV2DTO> pedidosDto = pedidoService.listarPedidosV2Abertos();

        // Verifica se a lista está vazia, retornando 204 No Content se não houver pedidos
        if (pedidosDto.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Retorna a lista de pedidos mapeados para DTO com status 200 OK
        return ResponseEntity.ok(pedidosDto);
    }

    @GetMapping("/pedidosFinalizados")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<PedidoV2DTO>> listarPedidosFinalizados() {
        // Chama o serviço para listar os pedidos abertos
        List<PedidoV2DTO> pedidosDto = pedidoService.listarPedidosV2Finalizados();

        // Verifica se a lista está vazia, retornando 204 No Content se não houver pedidos
        if (pedidosDto.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Retorna a lista de pedidos mapeados para DTO com status 200 OK
        return ResponseEntity.ok(pedidosDto);
    }

    //end point para buscar por uuid codigoPedido
    @GetMapping("/{codigoPedido}")
    public ResponseEntity<PedidoV2DTO> buscarPorCodigoPedido(@PathVariable UUID codigoPedido) {
        PedidoV2DTO pedido = pedidoService.buscarPorCodigoPedido(codigoPedido);

        return ResponseEntity.ok(pedido);
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<PedidoV2DTO>> listarPedidosV2() {
        List<PedidoV2DTO> pedidosDto = pedidoService.listarPedidosV2();

        if (pedidosDto.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(200).body(pedidosDto);
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



    @PostMapping("/finalizarPedido/{pedidoId}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<PedidoV2> atualizarStatus(@PathVariable Integer pedidoId) {
        PedidoV2 pedidoFinalizado = pedidoService.finalizarPedidoV2(pedidoId);

        return ResponseEntity.ok(pedidoFinalizado);
    }


}
