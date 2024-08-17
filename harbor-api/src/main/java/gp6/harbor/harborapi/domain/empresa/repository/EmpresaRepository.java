package gp6.harbor.harborapi.domain.empresa.repository;

import gp6.harbor.harborapi.domain.empresa.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {

    List<Empresa> findByRazaoSocialContainingIgnoreCase(String razaoSocial);
    Optional<Empresa> findByCnpj(String cnpj);
    boolean existsByCnpj(String cnpj);

    @Modifying
    @Transactional
    @Query("update Empresa e set e.foto = ?2 where e.id = ?1")
    void setFoto(Integer id, byte[] foto);

    @Query("select e.foto from Empresa e where e.id = ?1")
    byte[] getFoto(Integer id);
}
