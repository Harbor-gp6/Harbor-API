package gp6.harbor.harborapi.domain.prestador.repository;

import gp6.harbor.harborapi.domain.prestador.Prestador;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PrestadorRepository extends JpaRepository<Prestador, Integer> {
    boolean existsByCpf(String cpf);
}
