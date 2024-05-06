package gp6.harbor.harborapi.api.controller.pedido;

import gp6.harbor.harborapi.domain.pedido.Pedido;
import gp6.harbor.harborapi.domain.pedido.repository.PedidoRepository;
import gp6.harbor.harborapi.domain.prestador.repository.PrestadorRepository;
import gp6.harbor.harborapi.domain.produto.Produto;
import gp6.harbor.harborapi.domain.produto.repository.ProdutoRepository;
import gp6.harbor.harborapi.domain.servico.Servico;
import gp6.harbor.harborapi.domain.servico.repository.ServicoRepository;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import gp6.harbor.harborapi.service.pedido.dto.PedidoCriacaoDto;
import gp6.harbor.harborapi.service.pedido.dto.PedidoListagemDto;
import gp6.harbor.harborapi.service.pedido.dto.PedidoMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final ServicoRepository servicoRepository;
    private final PrestadorRepository prestadorRepository;

    @PostMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<PedidoListagemDto> criarPedido(@RequestBody @Valid PedidoCriacaoDto novoPedido) {
        Pedido pedido = PedidoMapper.toEntity(novoPedido, servicoRepository, produtoRepository);

        Optional<Prestador> prestador = prestadorRepository.findById(Long.valueOf(novoPedido.getPrestadorId()));

        if (prestador.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        pedido.setPrestador(prestador.get());

        Pedido pedidoSalvo = pedidoRepository.save(pedido);


        PedidoListagemDto listagemDto = PedidoMapper.toDto(pedidoSalvo);
        return ResponseEntity.status(201).body(listagemDto);
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<PedidoListagemDto>> listarPedidos() {
        List<Pedido> pedidos = pedidoRepository.findAll();

        if (pedidos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(200).body(PedidoMapper.toDto(pedidos));
    }

    @GetMapping("/prestador")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<PedidoListagemDto>> listarPorNomePrestador(@RequestParam String prestador) {
        List<Pedido> pedidos = pedidoRepository.listarPorPrestador(prestador);

        if (pedidos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().body(PedidoMapper.toDto(pedidos));
    }

    @GetMapping("/total-ganho")
    public ResponseEntity<Double> calcularTotalGanho() {
        List<Pedido> pedidos = pedidoRepository.findAll();

        if (pedidos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(totalGanho(pedidos, 0));
    }

    private Double totalGanho(List<Pedido> listaPedidos, int indice) {
        if (indice == listaPedidos.size()) {
            return 0.0;
        }

        Double total = 0.0;

        List<Produto> produtos = listaPedidos.get(indice).getListaProduto();
        List<Servico> servicos = listaPedidos.get(indice).getListaServico();

        if (!produtos.isEmpty()) {
            for (Produto p : produtos) {
                total += p.getPrecoVenda();
            }
        }

        if (!servicos.isEmpty()) {
            for (Servico s : servicos) {
                total += s.getValorServico();
            }
        }

        return total + totalGanho(listaPedidos, indice + 1);
    }

}
