package gp6.harbor.harborapi.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import gp6.harbor.harborapi.domain.cliente.Cliente;
import gp6.harbor.harborapi.domain.cliente.repository.ClienteRepository;
import gp6.harbor.harborapi.exception.ConflitoException;
import gp6.harbor.harborapi.exception.NaoEncontradoException;
import gp6.harbor.harborapi.service.cliente.ClienteService;

public class ClienteServiceTest {
    @Mock
    private ClienteRepository repository;

    @InjectMocks
    private ClienteService service;

    public ClienteServiceTest(){
        MockitoAnnotations.openMocks(this);
    }

    Cliente setUp() {
        Cliente cliente = new Cliente();
        cliente.setId(1);
        cliente.setNome("Henrique");
        cliente.setSobrenome("Mosca");
        cliente.setCpf("20487594815");
        cliente.setTelefone("11985478123");

        return cliente;
    }

    Cliente setup2(){
        Cliente cliente = new Cliente();
        cliente.setNome("Vitor");
        cliente.setSobrenome("Cardoso");
        cliente.setCpf("20487594815");
        cliente.setTelefone("11985473245");

        return cliente;
    }

    @Test
    @DisplayName("Cenario de buscar todos os clientes")
    void buscarTodos(){
        List<Cliente> clientes = List.of(
            new Cliente(),
            new Cliente()
        );

        clientes.get(0).setId(1);
        clientes.get(0).setNome("Henrique");
        clientes.get(0).setSobrenome("Mosca");
        clientes.get(0).setCpf("20487594815");
        clientes.get(0).setTelefone("11985478123");

        
        clientes.get(1).setId(2);
        clientes.get(1).setNome("Vitor");
        clientes.get(1).setSobrenome("Cardoso");
        clientes.get(1).setCpf("20465794815");
        clientes.get(1).setTelefone("11985479874");

        when(repository.findAll()).thenReturn(clientes);
        
        List<Cliente> resposta = service.buscarTodos();

        assertEquals(clientes.size(), resposta.size());
        assertEquals(clientes, resposta);

        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Cenario de Buscar por Id de cliente existente")
    void buscarPorIdCorreto(){
        Cliente cliente = setUp();
        Integer idInformado = 1;

        when(repository.findById(idInformado)).thenReturn(Optional.of(cliente));

        Cliente resposta = service.buscarPorId(idInformado);

        assertEquals(idInformado, resposta.getId());
        assertEquals(cliente.getNome(), resposta.getNome());

        verify(repository, times(0)).findAll();
        verify(repository, times(1)).findById(idInformado);
    }

    @Test
    @DisplayName("Cenario de Buscar por Id de cliente não existente")
    void buscarPorIdIncorreto(){
        Integer idInformado = 1;

        when(repository.findById(idInformado)).thenReturn(Optional.empty());

        assertThrows(NaoEncontradoException.class,
        () -> service.buscarPorId(idInformado));

        
        verify(repository, times(0)).findAll();
        verify(repository, times(1)).findById(idInformado);
    }

    @Test
    @DisplayName("Cenario de Buscar por Id de cliente existente")
    void buscarPorNome(){
        List<Cliente> clientes = List.of(
            new Cliente()
        );
        clientes.get(0).setId(1);
        clientes.get(0).setNome("Henrique");
        clientes.get(0).setSobrenome("Mosca");
        clientes.get(0).setCpf("20487594815");
        clientes.get(0).setTelefone("11985478123");
        repository.save(clientes.get(0));

        String nomeInformado = "Henrique";

        when(repository.findByNomeContainsIgnoreCase(clientes.get(0).getNome())).thenReturn(clientes);

        List<Cliente> resposta = service.buscarPorNome(nomeInformado);

        assertEquals(clientes, resposta);
        assertEquals(clientes.get(0).getNome(), resposta.get(0).getNome());

        verify(repository, times(0)).findAll();
        verify(repository, times(1)).findByNomeContainsIgnoreCase(nomeInformado);
    }

    @Test
    @DisplayName("Cenario de cadastramento de cliente correto")
    void cadastroCorreto(){
        Cliente cliente = setUp();

        when(repository.save(cliente)).thenReturn(cliente);

        Cliente resultado = service.cadastrar(cliente);

        assertNotNull(resultado);
        assertEquals(cliente, resultado);

        verify(repository).save(cliente);
    }

    @Test
    @DisplayName("Cenario de cadastramento de cliente com conflito")
    void CadastroIncorreto(){
        Cliente cliente = setUp();
        repository.save(cliente);

        Cliente cliente2 = setup2();

        when(repository.save(cliente2)).thenThrow(ConflitoException.class);

        assertThrows(ConflitoException.class,
        () -> service.cadastrar(cliente2));
    }

    @Test
    @DisplayName("Cenario de atualização de cliente")
    void atualizarCliente(){
        Cliente cliente = setUp();
        repository.save(cliente);

        Cliente cliente2 = setup2();
        cliente2.setId(1);

        when(repository.existsById(cliente2.getId())).thenReturn(true);
        when(repository.save(cliente2)).thenReturn(cliente2);

        Cliente resposta = service.atualizar(cliente2);

        assertEquals(cliente2, resposta);
        assertNotEquals(cliente, resposta);
    }

    @Test
    @DisplayName("Cenario de delete de um cliente existente")
    void deletarCliente(){
        Cliente cliente = setUp();
        when(repository.existsById(cliente.getId())).thenReturn(true);

        repository.deleteById(cliente.getId());

        verify(repository, times(1)).deleteById(cliente.getId());
    }
}