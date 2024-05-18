package gp6.harbor.harborapi.service.empresa;

import gp6.harbor.harborapi.domain.cliente.Cliente;
import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.empresa.repository.EmpresaRepository;
import gp6.harbor.harborapi.exception.ConflitoException;
import gp6.harbor.harborapi.exception.NaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmpresaService {

    private final EmpresaRepository empresaRepository;

    public Empresa cadastrar(Empresa empresa) {
        if (existePorCnpj(empresa.getCnpj())) {
            throw new ConflitoException("Empresa");
        }

        return empresaRepository.save(empresa);
    }

    public List<Empresa> listar() { return empresaRepository.findAll(); }

    public Empresa buscarPorId(int id) {
        return empresaRepository.findById(id).orElseThrow(() -> new NaoEncontradoException("Cliente"));
    }

    public boolean existePorCnpj(String cnpj) {
        return empresaRepository.existsByCnpj(cnpj);
    }

    public boolean existePorId(Integer id) {
        return empresaRepository.existsById(id);
    }

    public void inativarEmpresa(int id) {
        Optional<Empresa> empresaOptional = empresaRepository.findById(id);
        if (empresaOptional.isPresent()) {
            Empresa empresa = empresaOptional.get();
            empresa.setDataInativacao(LocalDate.now());
            empresaRepository.save(empresa);
        } else {
            throw new NaoEncontradoException("Empresa");
        }
    }
}
