package gp6.harbor.harborapi.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.empresa.repository.EmpresaRepository;
import gp6.harbor.harborapi.domain.endereco.Endereco;
import gp6.harbor.harborapi.exception.ConflitoException;
import gp6.harbor.harborapi.exception.NaoEncontradoException;
import gp6.harbor.harborapi.service.empresa.EmpresaService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmpresaServiceTest {
    @Mock
    private EmpresaRepository repository;

    @InjectMocks
    private EmpresaService service;

    public EmpresaServiceTest(){
        MockitoAnnotations.openMocks(this);
    }

    Empresa setUp(){
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

        return empresa;
    }

    List<Empresa> setUpLista(){
        List<Empresa> empresas = new ArrayList<>();

        Endereco endereco1 = new Endereco();
        endereco1.setId(1);
        endereco1.setLogradouro("Rua Exemplo 1");
        endereco1.setNumero("123");
        endereco1.setCidade("Cidade Exemplo 1");
        endereco1.setEstado("Estado Exemplo 1");
        endereco1.setCep("12345-678");

        Empresa empresa1 = new Empresa();
        empresa1.setId(1);
        empresa1.setRazaoSocial("Razão Social Exemplo 1");
        empresa1.setNomeFantasia("Nome Fantasia Exemplo 1");
        empresa1.setCnpj("12.345.678/0001-91");
        empresa1.setEndereco(endereco1);
        empresa1.setDataCriacao(LocalDate.now());
        empresa1.setHorarioAbertura(LocalTime.of(8, 0));
        empresa1.setHorarioFechamento(LocalTime.of(18, 0));
        empresa1.setDataInativacao(null);

        Endereco endereco2 = new Endereco();
        endereco2.setId(2);
        endereco2.setLogradouro("Rua Exemplo 2");
        endereco2.setNumero("456");
        endereco2.setCidade("Cidade Exemplo 2");
        endereco2.setEstado("Estado Exemplo 2");
        endereco2.setCep("87654-321");

        Empresa empresa2 = new Empresa();
        empresa2.setId(2);
        empresa2.setRazaoSocial("Razão Social Exemplo 2");
        empresa2.setNomeFantasia("Nome Fantasia Exemplo 2");
        empresa2.setCnpj("98.765.432/0001-92");
        empresa2.setEndereco(endereco2);
        empresa2.setDataCriacao(LocalDate.now());
        empresa2.setHorarioAbertura(LocalTime.of(9, 0));
        empresa2.setHorarioFechamento(LocalTime.of(17, 0));
        empresa2.setDataInativacao(null);

        empresas.add(empresa1);
        empresas.add(empresa2);

        return empresas;
    }

    @Test
    @DisplayName("Cenario de cadastramento de empresa correto")
    void cadastroCorreto(){
        Empresa empresa = setUp();

        when(repository.save(empresa)).thenReturn(empresa);

        Empresa resposta = service.cadastrar(empresa);

        assertEquals(empresa, resposta);
        verify(repository, times(1)).save(empresa);
    }

    @Test
    @DisplayName("Cenario de cadastramento de empresa cnpj duplicado")
    void CadastroIncorreto(){
        Empresa empresa = setUp();
        repository.save(empresa);
        Empresa empresa2 = setUp();

        when(repository.save(empresa2)).thenThrow(ConflitoException.class);
        
        assertThrows(ConflitoException.class,
        () -> service.cadastrar(empresa2));
    }

    @Test
    @DisplayName("Cenario de listagem de empresas")
    void listar(){
        List<Empresa> empresas = setUpLista();

        when(repository.findAll()).thenReturn(empresas);

        List<Empresa> resposta = service.listar();

        assertEquals(empresas, resposta);
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Cenario de busca por Id Encontrado")
    void buscarPorId(){
        Empresa empresa = setUp();
        Integer idInformado = 1;

        when(repository.findById(idInformado)).thenReturn(Optional.of(empresa));

        Empresa resposta = service.buscarPorId(idInformado);

        assertEquals(idInformado, resposta.getId());
        assertEquals(empresa.getNomeFantasia(), resposta.getNomeFantasia());

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
    @DisplayName("Cenario para inativação de empresa concluida")
    void inativarEmpresa(){
        Empresa empresa = setUp();

        when(repository.save(empresa)).thenReturn(empresa);
        empresa.setDataInativacao(LocalDate.now());

        repository.save(empresa);
        
        assertNotNull(empresa.getDataInativacao());
        verify(repository, times(1)).save(empresa);
    }
}