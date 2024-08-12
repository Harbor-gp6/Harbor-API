package gp6.harbor.harborapi.domain.produto.repository;

import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import gp6.harbor.harborapi.domain.produto.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
    List<Produto> findAllByOrderByPrecoVendaDesc();
    List<Produto> findAllByOrderByPrecoVendaAsc();
    List<Produto> findByIdIn(List<Integer> ids);
    List<Produto> findByEmpresa(Empresa empresa);

}
