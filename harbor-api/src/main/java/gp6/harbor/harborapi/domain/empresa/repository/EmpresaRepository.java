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
    Optional<Empresa> findBySlug(String slug);
    Optional<Empresa> findByCnpj(String cnpj);
    boolean existsByCnpj(String cnpj);

    @Modifying
    @Transactional
    @Query("update Empresa e set e.foto = ?2 where e.id = ?1")
    void setFoto(Integer id, String foto);

    @Query("select e.foto from Empresa e where e.id = ?1")
    String getFoto(Integer id);

    @Modifying
    @Transactional
    @Query("update Empresa e set e.banner = ?2 where e.id = ?1")
    void setBanner(Integer id, String banner);

    @Query("select e.banner from Empresa e where e.id = ?1")
    String getBanner(Integer id);
}
