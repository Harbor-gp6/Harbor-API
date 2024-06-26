package gp6.harbor.harborapi.domain.cliente.repository;

import gp6.harbor.harborapi.domain.cliente.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    List<Cliente> findByNomeContainsIgnoreCase(String nome);

    boolean existsByCpf(String cpf);
}
