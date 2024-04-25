package gp6.harbor.harborapi.domain.endereco.repository;

import gp6.harbor.harborapi.domain.endereco.Endereco;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {
    List<Endereco> findByCep(String cep);

}