package gp6.harbor.harborapi.domain.servico.repository;

import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.servico.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository
public interface ServicoRepository extends JpaRepository<Servico, Integer>{

    List<Servico> findByIdIn(List<Integer> ids);
    List<Servico> findByEmpresa(Empresa empresa);

    boolean existsByDescricaoServicoAndEmpresa(String nome, Empresa empresa);

    @Modifying
    @Transactional
    @Query("update Servico s set s.foto = ?2 where s.id = ?1")
    void setFoto(Integer id, byte[] foto);

    @Query("select s.foto from Servico s where s.id = ?1")
    byte[] getFoto(Integer id);
}
