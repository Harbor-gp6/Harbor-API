package gp6.harbor.harborapi.service.produto;

import gp6.harbor.harborapi.domain.produto.Produto;
import gp6.harbor.harborapi.domain.produto.repository.ProdutoRepository;
import gp6.harbor.harborapi.exception.NaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public Produto cadastrar(Produto novoProduto) {
        return produtoRepository.save(novoProduto);
    }

    public Produto buscarPorId(int id) {
        return produtoRepository.findById(id).orElseThrow(() -> new NaoEncontradoException("Produto"));
    }

    public List<Produto> listar() {
        return produtoRepository.findAll();
    }

    public boolean existePorId(Integer id) {
        return produtoRepository.existsById(id);
    }

    public List<Produto> buscarMaiorParaMenor() {
        return produtoRepository.findAllByOrderByPrecoVendaDesc();
    }

    public List<Produto> buscarMenorParaMaior() {
        return produtoRepository.findAllByOrderByPrecoVendaAsc();
    }

    public Produto deletarPorId(int id) {
        Optional<Produto> produtoOptional = produtoRepository.findById(id);
        if (produtoOptional.isPresent()) {
            Produto produto = produtoOptional.get();
            produtoRepository.deleteById(id);
            return produto;
        } else {
            throw new NaoEncontradoException("Produto");
        }
    }

    public List<Produto> buscarTodosPorId(List<Integer> ids) {
        return produtoRepository.findByIdIn(ids);
    }
}
