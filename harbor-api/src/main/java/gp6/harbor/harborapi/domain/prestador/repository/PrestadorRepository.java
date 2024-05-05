package gp6.harbor.harborapi.domain.prestador.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import gp6.harbor.harborapi.domain.prestador.Prestador;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrestadorRepository extends JpaRepository<Prestador, Long> {

    Optional<Prestador> findByEmail(String email);

    List<Prestador> findByNomeContainsIgnoreCase(String nome);
    List<Prestador> findByCpf(String cpf);

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);


}
