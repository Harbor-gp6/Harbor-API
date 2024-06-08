package gp6.harbor.harborapi.service;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import gp6.harbor.harborapi.api.enums.CargoEnum;
import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.endereco.Endereco;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import gp6.harbor.harborapi.domain.prestador.repository.PrestadorRepository;
import gp6.harbor.harborapi.exception.ConflitoException;
import gp6.harbor.harborapi.exception.NaoEncontradoException;
import gp6.harbor.harborapi.service.pedido.PedidoService;
import gp6.harbor.harborapi.service.prestador.PrestadorService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PrestadorServiceTest {

    @Mock
    private PrestadorRepository repository;

    @Mock
    private PedidoService pedidoService;

    @InjectMocks
    private PrestadorService prestadorService;

    Prestador setUp() {

        Endereco endereco = new Endereco();
        endereco.setId(1);
        endereco.setLogradouro("Rua Exemplo");
        endereco.setNumero("123");
        endereco.setCidade("Cidade Exemplo");
        endereco.setEstado("Estado Exemplo");
        endereco.setCep("12345-678");

        Empresa empresa = new Empresa();
        empresa.setId(1);
        empresa.setRazaoSocial("Razão Social Exemplo");
        empresa.setNomeFantasia("Nome Fantasia Exemplo");
        empresa.setCnpj("12.345.678/0001-90");
        empresa.setEndereco(endereco);
        empresa.setDataCriacao(LocalDate.now());
        empresa.setHorarioAbertura(LocalTime.of(8, 0));
        empresa.setHorarioFechamento(LocalTime.of(18, 0));

        Prestador prestador = new Prestador();
        prestador.setId(1L);
        prestador.setNome("Nome Prestador");
        prestador.setSobrenome("Sobrenome Prestador");
        prestador.setTelefone("123456789");
        prestador.setCpf("12345678900");
        prestador.setFoto(new byte[0]);
        prestador.setEmail("email@exemplo.com");
        prestador.setSenha("senhaSegura");
        prestador.setEmpresa(empresa);
        prestador.setCargo(CargoEnum.ADMIN);

        return prestador;
    }

    @Test
    @DisplayName("Cenário de criar prestador com sucesso")
    void criarPrestadorComSucesso() {
        Prestador prestador = setUp();

        when(repository.existsById(prestador.getId())).thenReturn(false);
        when(repository.save(prestador)).thenReturn(prestador);

        Prestador resposta = prestadorService.criar(prestador);

        assertEquals(prestador, resposta);
        verify(repository, times(1)).save(prestador);
    }

    @Test
    @DisplayName("Cenário de criar prestador que já existe")
    void criarPrestadorJaExistente() {
        Prestador prestador = setUp();
        when(repository.existsById(prestador.getId())).thenReturn(true);

        assertThrows(ConflitoException.class, () -> prestadorService.criar(prestador));
        verify(repository, times(0)).save(prestador);
    }

    @Test
    @DisplayName("Cenário de listar todos os prestadores")
    void listarTodosOsPrestadores() {
        Prestador prestador = setUp();
        List<Prestador> prestadores = Arrays.asList(prestador);
        when(repository.findAll()).thenReturn(prestadores);

        List<Prestador> resposta = prestadorService.listar();

        assertEquals(prestadores, resposta);
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Cenário de buscar prestador por ID com sucesso")    void buscarPrestadorPorIdComSucesso() {
        Prestador prestador = setUp();
        when(repository.findById(prestador.getId())).thenReturn(Optional.of(prestador));

        Prestador resposta = prestadorService.buscarPorId(prestador.getId());

        assertEquals(prestador, resposta);
        verify(repository, times(1)).findById(prestador.getId());
    }

    @Test
    @DisplayName("Cenário de buscar prestador por ID não encontrado")
    void buscarPrestadorPorIdNaoEncontrado() {
        Prestador prestador = setUp();
        when(repository.findById(prestador.getId())).thenReturn(Optional.empty());

        assertThrows(NaoEncontradoException.class, () -> prestadorService.buscarPorId(prestador.getId()));
        verify(repository, times(1)).findById(prestador.getId());
    }

    @Test
    @DisplayName("Cenário de buscar prestador pelo CPF")
    void buscarPrestadorPeloCpf() {
        Prestador prestador = setUp();
        List<Prestador> prestadores = Arrays.asList(prestador);
        when(repository.findByCpfLike(prestador.getCpf())).thenReturn(prestadores);

        List<Prestador> resposta = prestadorService.buscarPeloCpf(prestador.getCpf());

        assertEquals(prestadores, resposta);
        verify(repository, times(1)).findByCpfLike(prestador.getCpf());
    }

    @Test
    @DisplayName("Cenário de verificar se prestador existe pelo CPF")
    void verificarSePrestadorExistePeloCpf() {
        Prestador prestador = setUp();
        when(repository.existsByCpf(prestador.getCpf())).thenReturn(true);

        boolean resposta = prestadorService.existePorCpf(prestador.getCpf());

        assertTrue(resposta);
        verify(repository, times(1)).existsByCpf(prestador.getCpf());
    }

    @Test
    @DisplayName("Cenário de buscar prestador pelo nome")
    void buscarPrestadorPeloNome() {
        Prestador prestador = setUp();
        List<Prestador> prestadores = Arrays.asList(prestador);
        when(repository.findByNomeContainsIgnoreCase(prestador.getNome())).thenReturn(prestadores);

        List<Prestador> resposta = prestadorService.buscarPeloNome(prestador.getNome());

        assertEquals(prestadores, resposta);
        verify(repository, times(1)).findByNomeContainsIgnoreCase(prestador.getNome());
    }

    @Test
    @DisplayName("Cenário de buscar prestador por empresa")
    void buscarPrestadorPorEmpresa() {
        Prestador prestador = setUp();
        List<Prestador> prestadores = Arrays.asList(prestador);
        when(repository.findByEmpresa(prestador.getEmpresa())).thenReturn(prestadores);

        List<Prestador> resposta = prestadorService.buscarPorEmpresa(prestador.getEmpresa());

        assertEquals(prestadores, resposta);
        verify(repository, times(1)).findByEmpresa(prestador.getEmpresa());
    }

    @Test
    @DisplayName("Cenário de verificar se prestador existe por ID")
    void verificarSePrestadorExistePorId() {
        Prestador prestador = setUp();
        when(repository.existsById(prestador.getId())).thenReturn(true);

        boolean resposta = prestadorService.existePorId(prestador.getId());

        assertTrue(resposta);
        verify(repository, times(1)).existsById(prestador.getId());
    }
}