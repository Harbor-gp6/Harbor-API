package gp6.harbor.harborapi.domain.cliente.repository;

import gp6.harbor.harborapi.domain.cliente.Cliente;
import gp6.harbor.harborapi.domain.empresa.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    List<Cliente> findByNomeContainsIgnoreCase(String nome);
    Optional<Cliente> findByCpf(String cpf);
    List<Cliente> findByEmpresa(Empresa empresa);

    boolean existsByCpf(String cpf);
}
