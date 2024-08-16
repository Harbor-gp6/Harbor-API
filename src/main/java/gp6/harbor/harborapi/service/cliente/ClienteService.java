package gp6.harbor.harborapi.service.cliente;

import gp6.harbor.harborapi.domain.cliente.Cliente;
import gp6.harbor.harborapi.domain.cliente.repository.ClienteRepository;
import gp6.harbor.harborapi.exception.ConflitoException;
import gp6.harbor.harborapi.exception.NaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public List<Cliente> buscarTodos() {
        return clienteRepository.findAll();
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
}
