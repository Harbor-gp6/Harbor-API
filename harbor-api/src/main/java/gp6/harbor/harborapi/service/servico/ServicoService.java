package gp6.harbor.harborapi.service.servico;

import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.servico.Servico;
import gp6.harbor.harborapi.domain.servico.repository.ServicoRepository;
import gp6.harbor.harborapi.exception.NaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicoService {

    private final ServicoRepository servicoRepository;

    public Servico buscaPorId(Integer id) {
        return servicoRepository.findById(id).orElseThrow(() -> new NaoEncontradoException("Servico"));
    }

    public List<Servico> buscaTodosPorIds(List<Integer> ids) {
        return servicoRepository.findByIdIn(ids);
    }

    public List<Servico> buscaPorEmpresa(Empresa empresa) {
        return servicoRepository.findByEmpresa(empresa);
    }

}
