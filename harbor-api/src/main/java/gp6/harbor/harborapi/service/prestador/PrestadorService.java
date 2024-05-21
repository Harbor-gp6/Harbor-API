package gp6.harbor.harborapi.service.prestador;

import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import gp6.harbor.harborapi.domain.prestador.repository.PrestadorRepository;
import gp6.harbor.harborapi.exception.ConflitoException;
import gp6.harbor.harborapi.exception.NaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrestadorService {

    private final PrestadorRepository prestadorRepository;

    public Prestador criar(Prestador prestador) {
        if (prestadorRepository.existsById(prestador.getId())) {
            throw new ConflitoException("Prestador Id");
        }
        return prestadorRepository.save(prestador);
    }
    
    public List<Prestador> listar() {
        return prestadorRepository.findAll();
    }

    public Prestador buscarPorId(Long id) {
        return prestadorRepository.findById(id).orElseThrow(
                () -> new NaoEncontradoException("Prestador")
        );
    }

    public List<Prestador> buscarPeloCpf(String cpf) {
        return prestadorRepository.findByCpf(cpf);
    }

    public List<Prestador> buscarPeloNome(String nome) {
        return prestadorRepository.findByNomeContainsIgnoreCase(nome);
    }

    public List<Prestador> buscarPorEmpresa(Empresa empresa) {
        return prestadorRepository.findByEmpresa(empresa);
    }
     
    public boolean existePorId(Long id) {
        return prestadorRepository.existsById(id);
    }
    
    
}
