package gp6.harbor.harborapi.domain.prestador.repository;

import gp6.harbor.harborapi.domain.prestador.AvaliacaoPrestador;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvaliacaoRepository extends JpaRepository<AvaliacaoPrestador, Long> {
    //criar consultas que acha uteis
    List<AvaliacaoPrestador> findByPrestadorId(Long id);
    List<AvaliacaoPrestador> findByClienteId(Integer id);
    List<AvaliacaoPrestador> findByCnpjEmpresa(String cnpj);

}
