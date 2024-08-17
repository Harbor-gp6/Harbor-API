package gp6.harbor.harborapi.service;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import gp6.harbor.harborapi.domain.endereco.Endereco;
import gp6.harbor.harborapi.domain.endereco.repository.EnderecoRepository;
import gp6.harbor.harborapi.exception.NaoEncontradoException;
import gp6.harbor.harborapi.service.endereco.EnderecoService;

public class EnderecoServiceTest {
    @Mock
    private EnderecoRepository repository;

    @InjectMocks
    private EnderecoService service;

    public EnderecoServiceTest(){
        MockitoAnnotations.openMocks(this);
    }

    Endereco setUp(){
        Endereco endereco = new Endereco();
        endereco.setId(1);
        endereco.setLogradouro("Rua Exemplo");
        endereco.setNumero("123");
        endereco.setCidade("Cidade Exemplo");
        endereco.setEstado("Estado Exemplo");
        endereco.setCep("12345-678");

        return endereco;
    }

    List<Endereco> setUpLista(){
        List<Endereco> enderecos = new ArrayList<>();

        Endereco endereco = new Endereco();
        endereco.setId(1);
        endereco.setLogradouro("Rua Exemplo");
        endereco.setNumero("123");
        endereco.setCidade("Cidade Exemplo");
        endereco.setEstado("Estado Exemplo");
        endereco.setCep("12345-678");

        Endereco endereco2 = new Endereco();
        endereco2.setId(2);
        endereco2.setLogradouro("Rua Exemplo 2");
        endereco2.setNumero("456");
        endereco2.setCidade("Cidade Exemplo 2");
        endereco2.setEstado("Estado Exemplo 2");
        endereco2.setCep("87654-321");

        enderecos.add(endereco);
        enderecos.add(endereco2);

        return enderecos;
    }

    @Test
    @DisplayName("Cenario de cadastramento de endereços")
    void cadastrarEndereco(){
        Endereco endereco = setUp();

        when(repository.save(endereco)).thenReturn(endereco);

        Endereco resposta = service.cadastrar(endereco);

        assertEquals(endereco, resposta);

        verify(repository, times(1)).save(endereco);
    }

    @Test
    @DisplayName("Cenario de busca por Id Encontrado")
    void buscarPorId(){
        Endereco endereco = setUp();
        Integer idInformado = 1;

        when(repository.findById(idInformado)).thenReturn(Optional.of(endereco));

        Endereco resposta = service.buscarPorId(idInformado);

        assertEquals(idInformado, resposta.getId());
        assertEquals(endereco.getLogradouro(), resposta.getLogradouro());

        verify(repository, times(0)).findAll();
        verify(repository, times(1)).findById(idInformado);
    }

    @Test
    @DisplayName("Cenario de Buscar por Id de empresa não existente")
    void buscarPorIdIncorreto(){
        Integer idInformado = 1;

        when(repository.findById(idInformado)).thenReturn(Optional.empty());

        assertThrows(NaoEncontradoException.class,
        () -> service.buscarPorId(idInformado));
   
        verify(repository, times(0)).findAll();
        verify(repository, times(1)).findById(idInformado);
    }

    @Test
    @DisplayName("Cenario de busca por Cep")
    void buscarPorCep(){
        List<Endereco> enderecos = setUpLista();
        repository.save(enderecos.get(0));

        when(repository.findByCep(enderecos.get(0).getCep())).thenReturn(enderecos);

        List<Endereco> resposta = service.buscarPorCep(enderecos.get(0).getCep());

        assertEquals(enderecos, resposta);

        verify(repository, times(0)).findAll();
        verify(repository, times(1)).findByCep(enderecos.get(0).getCep());
    }

    @Test
    @DisplayName("Cenario de listagem de todos os endereços")
    void listar(){
        List<Endereco> enderecos = setUpLista();

        when(repository.findAll()).thenReturn(enderecos);

        List<Endereco> resposta = service.listar();

        assertEquals(enderecos, resposta);
        verify(repository, times(1)).findAll();
    }
}