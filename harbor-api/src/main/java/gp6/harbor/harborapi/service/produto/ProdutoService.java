package gp6.harbor.harborapi.service.produto;

import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import gp6.harbor.harborapi.domain.prestador.repository.PrestadorRepository;
import gp6.harbor.harborapi.domain.produto.Produto;
import gp6.harbor.harborapi.domain.produto.repository.ProdutoRepository;
import gp6.harbor.harborapi.exception.NaoEncontradoException;
import gp6.harbor.harborapi.service.empresa.EmpresaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final PrestadorRepository prestadorRepository;

    public Produto cadastrar(Produto novoProduto) {
        return produtoRepository.save(novoProduto);
    }

    public Produto buscarPorId(int id) {
        return produtoRepository.findById(id).orElseThrow(() -> new NaoEncontradoException("Produto"));
    }

    public List<Produto> listar() {
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();

        Prestador prestador = prestadorRepository.findByEmail(emailUsuario).orElse(null);

        Empresa empresa = prestador.getEmpresa();

        return produtoRepository.findByEmpresa(empresa);
    }

    public List<Produto> buscarMaiorParaMenor() {
        return produtoRepository.findAllByOrderByPrecoVendaDesc();
    }

    public List<Produto> buscarMenorParaMaior() {
        return produtoRepository.findAllByOrderByPrecoVendaAsc();
    }

    public void deletarPorId(int id) {
        buscarPorId(id);
        produtoRepository.deleteById(id);
    }

    public List<Produto> buscarTodosPorId(List<Integer> ids) {
        return produtoRepository.findByIdIn(ids);
    }
}
