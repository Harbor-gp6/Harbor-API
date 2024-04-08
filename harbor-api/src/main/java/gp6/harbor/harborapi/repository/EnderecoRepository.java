package gp6.harbor.harborapi.repository;

import gp6.harbor.harborapi.dto.EnderecoListagemDto;
import gp6.harbor.harborapi.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {
    List<Endereco> findByCep(String cep);
    List<Endereco> findByNome(String nome);
    boolean existsByRuaAndNumeroAndCidadeAndEstadoAndCep(String bairro, String logradouro, String cidade, String estado, String numero, String cep, String complemento);


}
