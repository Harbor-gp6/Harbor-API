package gp6.harbor.harborapi.service.cliente;

import gp6.harbor.harborapi.domain.cliente.Cliente;
import gp6.harbor.harborapi.domain.cliente.repository.ClienteRepository;
import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import gp6.harbor.harborapi.domain.prestador.repository.PrestadorRepository;
import gp6.harbor.harborapi.exception.ConflitoException;
import gp6.harbor.harborapi.exception.NaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PrestadorRepository prestadorRepository;

    public List<Cliente> buscarTodos() {
        //buscar apenas os clienets da empresa que esta requisitando
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Prestador prestador = prestadorRepository.findByEmail(emailUsuario).orElse(null);
        Empresa empresa = prestador.getEmpresa();

        return clienteRepository.findByEmpresa(empresa);
    }

    public Cliente buscarPorId(int id) {
        return clienteRepository.findById(id).orElseThrow(() -> new NaoEncontradoException("Cliente"));
    }

    public List<Cliente> buscarPorNome(String nome) {
        return clienteRepository.findByNomeContainsIgnoreCase(nome);
    }

    public Cliente cadastrar(Cliente cliente) {
        if (existePorCpf(cliente.getCpf())) {
            throw new ConflitoException("Cliente");
        }

        return clienteRepository.save(cliente);
    }

    public Cliente atualizar(Cliente cliente) {
        if (!clienteRepository.existsById(cliente.getId())) {
            throw new NaoEncontradoException("Cliente");
        }

        return clienteRepository.save(cliente);
    }

    public void deletar(int id) {
        if (!clienteRepository.existsById(id)) {
            throw new NaoEncontradoException("Cliente");
        }
        clienteRepository.deleteById(id);
    }

    public boolean existePorCpf(String cpf) {
        return clienteRepository.existsByCpf(cpf);
    }

    public boolean validarCliente(Cliente cliente){
        if (cliente.getNome() == null || cliente.getNome().isEmpty()) {
            return false;
        }
        if (cliente.getSobrenome() == null || cliente.getSobrenome().isEmpty()) {
            return false;
        }
        if (cliente.getCpf() == null || cliente.getCpf().isEmpty()) {
            return false;
        }
        if (cliente.getEmail() == null || cliente.getEmail().isEmpty()) {
            return false;
        }
        if (cliente.getTelefone() == null || cliente.getTelefone().isEmpty()) {
            return false;
        }
        return true;
    }
}
