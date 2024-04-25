package gp6.harbor.harborapi.domain.produto.repository;

import gp6.harbor.harborapi.domain.produto.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
    List<Produto> findAllByOrderByPrecoVendaDesc();
    List<Produto> findAllByOrderByPrecoVendaAsc();
}
