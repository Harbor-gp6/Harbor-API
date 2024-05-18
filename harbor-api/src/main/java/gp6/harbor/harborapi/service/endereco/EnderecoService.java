package gp6.harbor.harborapi.service.endereco;

import gp6.harbor.harborapi.domain.endereco.Endereco;
import gp6.harbor.harborapi.domain.endereco.repository.EnderecoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;

    public Endereco cadastrar(Endereco endereco) {
        return enderecoRepository.save(endereco);
    }
}
