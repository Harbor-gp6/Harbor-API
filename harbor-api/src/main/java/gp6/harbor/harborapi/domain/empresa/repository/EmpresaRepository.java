package gp6.harbor.harborapi.domain.empresa.repository;

import gp6.harbor.harborapi.domain.empresa.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {

    List<Empresa> findByRazaoSocialContainingIgnoreCase(String razaoSocial);
    Optional<Empresa> findByCnpj(String cnpj);
    boolean existsByCnpj(String cnpj);

}
