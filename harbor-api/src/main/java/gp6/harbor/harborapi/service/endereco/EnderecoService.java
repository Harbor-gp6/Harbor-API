package gp6.harbor.harborapi.service.endereco;

import gp6.harbor.harborapi.domain.endereco.Endereco;
import gp6.harbor.harborapi.domain.endereco.repository.EnderecoRepository;
import gp6.harbor.harborapi.exception.NaoEncontradoException;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;

    public Endereco cadastrar(Endereco endereco) {
        return enderecoRepository.save(endereco);
    }

    public Endereco buscarPorId(Integer id) {
        return enderecoRepository.findById(id).orElseThrow(() -> new NaoEncontradoException("Endereço"));
    }

    public List<Endereco> buscarPorCep(String cep) {
        return enderecoRepository.findByCep(cep);
    }

    public List<Endereco> listar() {
        return enderecoRepository.findAll();
    }

    //validar endereco
    public boolean validarEndereco(Endereco endereco) {
        return endereco.getEstado() != null && endereco.getCidade() != null && endereco.getBairro() != null && endereco.getLogradouro() != null && endereco.getNumero() != null && endereco.getCep() != null;
    }
}
