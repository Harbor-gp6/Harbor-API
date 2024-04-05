package gp6.harbor.harborapi.repository;

import gp6.harbor.harborapi.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmpresaRepository extends JpaRepository<Cliente, Integer> {

}
