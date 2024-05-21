package gp6.harbor.harborapi.domain.cliente.repository;

import gp6.harbor.harborapi.domain.cliente.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    List<Cliente> findByNomeContainsIgnoreCase(String nome);

    boolean existsByCpf(String cpf);

    @Modifying
    @Transactional
    @Query("update Cliente c set c.foto = ?2 where c.id = ?1")
    void setFoto(Integer id, byte[] foto);

    @Query("select c.foto from Cliente c where c.id = ?1")
    byte[] getFoto(Integer id);
}
