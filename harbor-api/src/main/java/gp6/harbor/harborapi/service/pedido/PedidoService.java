package gp6.harbor.harborapi.service.pedido;

import gp6.harbor.harborapi.api.controller.pedido.PedidoController;
import gp6.harbor.harborapi.domain.pedido.Pedido;
import gp6.harbor.harborapi.domain.pedido.PedidoProduto;
import gp6.harbor.harborapi.domain.pedido.PedidoServico;
import gp6.harbor.harborapi.domain.pedido.repository.PedidoRepository;
import gp6.harbor.harborapi.domain.produto.Produto;
import gp6.harbor.harborapi.domain.servico.Servico;
import gp6.harbor.harborapi.dto.pedido.dto.PedidoAtualizacaoProdutoDto;
import gp6.harbor.harborapi.exception.NaoEncontradoException;
import gp6.harbor.harborapi.exception.PedidoCapacidadeExcedidoException;
import gp6.harbor.harborapi.service.produto.ProdutoService;
import gp6.harbor.harborapi.service.servico.ServicoService;
import gp6.harbor.harborapi.util.PedidoFilaEspera;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoService produtoService;
    private final PedidoProdutoService pedidoProdutoService;
    private final PedidoServicoService pedidoServicoService;
    private final ServicoService servicoService;

    public Pedido criarPedido(Pedido novoPedido, List<Integer> servicosIds) {

        if(!PedidoController.filaPedido.adicionarPedido(novoPedido)) {
            throw new PedidoCapacidadeExcedidoException("Fila no limite de pedidos");
        }

        Pedido pedido = pedidoRepository.save(novoPedido);
        AtomicReference<Double> total = new AtomicReference<>(0.0);

        List<Servico> servicosDoBanco = servicoService.buscaTodosPorIds(servicosIds);

        List<PedidoServico> listPedidoServico = new ArrayList<>();

        servicosDoBanco.forEach(servico -> {
            PedidoServico pedidoServico = new PedidoServico();

            pedidoServico.setPedido(pedido);
            pedidoServico.setServico(servico);

            total.updateAndGet(v -> v + servico.getValorServico());

            listPedidoServico.add(pedidoServico);
        });

        pedido.setTotal(total.get());

        pedido.setPedidoServicos(pedidoServicoService.salvarTodos(listPedidoServico));

        return pedidoRepository.save(pedido);
    }

    public Pedido adicionarProduto(Integer pedidoId, PedidoAtualizacaoProdutoDto produtos) {
        Pedido pedidoEncontrado = pedidoRepository.findById(pedidoId).orElseThrow(() -> new NaoEncontradoException("Pedido"));

        List<Produto> produtosDoBanco = produtoService.buscarTodosPorId(produtos.getProdutos().stream().map(PedidoAtualizacaoProdutoDto.ProdutoDto::getId).toList());

        List<PedidoProduto> pedidoProdutos = new ArrayList<>();

        produtosDoBanco.forEach((produto) -> {
            PedidoProduto pedidoProduto = new PedidoProduto();
            pedidoProduto.setProduto(produto);
            pedidoProduto.setPedido(pedidoEncontrado);

            pedidoProdutos.add(pedidoProduto);
        });

        List<PedidoProduto> salvo = pedidoProdutoService.salvarTodos(pedidoProdutos);

        pedidoEncontrado.setPedidoProdutos(salvo);

        return pedidoRepository.save(pedidoEncontrado);
    }

    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    public List<Pedido> listarPorPrestador(String nomePrestador) {
        return pedidoRepository.listarPorPrestador(nomePrestador);
    }

    public List<Pedido> listarPorPrestadorId(Long prestadorId) {
        return pedidoRepository.findByPrestadorId(prestadorId);
    }
}
