package gp6.harbor.harborapi.repository;

import gp6.harbor.harborapi.entity.Cliente;
import gp6.harbor.harborapi.entity.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {

    List<Empresa> findByRazaoSocialContainingIgnoreCase(String razaoSocial);

    boolean existsByCnpj(String cnpj);

}
