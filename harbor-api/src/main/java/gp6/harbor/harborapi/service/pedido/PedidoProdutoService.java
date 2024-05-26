package gp6.harbor.harborapi.service.pedido;

import gp6.harbor.harborapi.domain.pedido.PedidoProduto;
import gp6.harbor.harborapi.domain.pedido.repository.PedidoProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoProdutoService {

    private final PedidoProdutoRepository pedidoProdutoRepository;

    public List<PedidoProduto> salvarTodos(List<PedidoProduto> pedidoProdutos) {
        return pedidoProdutoRepository.saveAll(pedidoProdutos);
    }
}
