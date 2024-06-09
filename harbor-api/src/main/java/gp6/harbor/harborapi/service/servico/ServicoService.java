package gp6.harbor.harborapi.service.servico;

import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.servico.Servico;
import gp6.harbor.harborapi.domain.servico.repository.ServicoRepository;
import gp6.harbor.harborapi.exception.NaoEncontradoException;
import gp6.harbor.harborapi.service.empresa.EmpresaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicoService {

    private final ServicoRepository servicoRepository;
    private final EmpresaService empresaService;

    public Servico buscaPorId(Integer id) {
        return servicoRepository.findById(id).orElseThrow(() -> new NaoEncontradoException("Servico"));
    }

    public Servico salvar(Servico servico) {
        return servicoRepository.save(servico);
    }

    public Servico atualizar(Integer empresaId, Integer id, Servico servicoParaAtualizar) {
        Servico servico = buscaPorId(id);
        Empresa empresa = empresaService.buscarPorId(empresaId);
        servicoParaAtualizar.setId(servico.getId());
        servicoParaAtualizar.setEmpresa(empresa);
        return salvar(servicoParaAtualizar);
    }

    public List<Servico> listar() {
        return servicoRepository.findAll();
    }

    public List<Servico> buscaTodosPorIds(List<Integer> ids) {
        return servicoRepository.findByIdIn(ids);
    }

    public List<Servico> buscaPorEmpresa(Empresa empresa) {
        return servicoRepository.findByEmpresa(empresa);
    }

    public void deletar(Integer id) {
        buscaPorId(id);
        servicoRepository.deleteById(id);
    }

}
