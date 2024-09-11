package gp6.harbor.harborapi.service.servico;

import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.empresa.repository.EmpresaRepository;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import gp6.harbor.harborapi.domain.prestador.repository.PrestadorRepository;
import gp6.harbor.harborapi.domain.servico.Servico;
import gp6.harbor.harborapi.domain.servico.repository.ServicoRepository;
import gp6.harbor.harborapi.exception.NaoEncontradoException;
import gp6.harbor.harborapi.service.empresa.EmpresaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicoService {

    private final ServicoRepository servicoRepository;
    private final PrestadorRepository prestadorRepository;
    private final EmpresaRepository empresaRepository;
    private final EmpresaService empresaService;

    public Servico buscaPorId(Integer id) {
        return servicoRepository.findById(id).orElseThrow(() -> new NaoEncontradoException("Servico"));
    }

    public Servico salvar(Servico servico) {
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Prestador prestador = prestadorRepository.findByEmail(emailUsuario).orElseThrow(() -> new NaoEncontradoException("Prestador"));
        Empresa empresa = prestador.getEmpresa();
        servico.setEmpresa(empresa);

        if (existe(servico)) {
            throw new IllegalArgumentException("Serviço já cadastrado");
        }

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
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();

        Prestador prestador = prestadorRepository.findByEmail(emailUsuario).orElse(null);

        Empresa empresa = prestador.getEmpresa();

        //return servicoRepository.findAll();
        return servicoRepository.findByEmpresa(empresa);
    }

    //buscar servico por objeto servico, ver se ja tem um servico igual aquele, com mesmo nome e empresa
    public boolean existe(Servico servico) {
        return servicoRepository.existsByDescricaoServicoAndEmpresa(servico.getDescricaoServico(), servico.getEmpresa());
    }


    public boolean existePorId(Integer id) {
        return servicoRepository.existsById(id);
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
    @Transactional
    public void setFoto(Integer id, byte[] novaFoto) {
        servicoRepository.setFoto(id, novaFoto);
    }

    public byte[] getFoto(Integer id) {
        return servicoRepository.getFoto(id);
    }
}
