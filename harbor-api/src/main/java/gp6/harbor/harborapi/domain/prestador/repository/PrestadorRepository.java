package gp6.harbor.harborapi.domain.prestador.repository;

import gp6.harbor.harborapi.domain.empresa.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrestadorRepository extends JpaRepository<Prestador, Long> {

    Optional<Prestador> findByEmail(String email);

    List<Prestador> findByNomeContainsIgnoreCase(String nome);
    List<Prestador> findByCpfLike(String cpf);

    Prestador findByCpf(String cpf);

    Boolean existsByCpf(String cpf);

    List<Prestador> findByEmpresa(Empresa empresa);

    //BUSCAR HORARIOSOCUPADOS QUE


    @Modifying
    @Transactional
    @Query("update Prestador p set p.foto = ?2 where p.id = ?1")
    void setFoto(Long id, String foto);

    @Query("select p.foto from Prestador p where p.id = ?1")
    String getFoto(Long id);
}
