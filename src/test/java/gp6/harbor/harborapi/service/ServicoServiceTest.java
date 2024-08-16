package gp6.harbor.harborapi.service;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.endereco.Endereco;
import gp6.harbor.harborapi.domain.servico.Servico;
import gp6.harbor.harborapi.domain.servico.repository.ServicoRepository;
import gp6.harbor.harborapi.exception.NaoEncontradoException;
import gp6.harbor.harborapi.service.servico.ServicoService;

public class ServicoServiceTest {
    @Mock
    private ServicoRepository repository;

    @InjectMocks
    private ServicoService service;

    public ServicoServiceTest(){
        MockitoAnnotations.openMocks(this);
    }

    Servico setup(){

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
        
        Servico servico = new Servico();
        servico.setId(1);
        servico.setDescricaoServico("Corte na régua");
        servico.setServicoEspecial(false);
        servico.setTempoMedioEmMinutos(20);
        servico.setEmpresa(empresa);

        return servico;
    }

    List<Servico> setUpLista(){
        List<Servico> servicos = new ArrayList<>();
        Endereco endereco1 = new Endereco();
        endereco1.setId(1);
        endereco1.setLogradouro("Rua Exemplo 1");
        endereco1.setNumero("123");
        endereco1.setCidade("Cidade Exemplo 1");
        endereco1.setEstado("Estado Exemplo 1");
        endereco1.setCep("12345-678");

        Empresa empresa = new Empresa();
        empresa.setId(1);
        empresa.setRazaoSocial("Razão Social Exemplo 1");
        empresa.setNomeFantasia("Nome Fantasia Exemplo 1");
        empresa.setCnpj("12.345.678/0001-91");
        empresa.setEndereco(endereco1);
        empresa.setDataCriacao(LocalDate.now());
        empresa.setHorarioAbertura(LocalTime.of(8, 0));
        empresa.setHorarioFechamento(LocalTime.of(18, 0));
        empresa.setDataInativacao(null);

        Endereco endereco2 = new Endereco();
        endereco2.setId(2);
        endereco2.setLogradouro("Rua Exemplo 2");
        endereco2.setNumero("456");
        endereco2.setCidade("Cidade Exemplo 2");
        endereco2.setEstado("Estado Exemplo 2");
        endereco2.setCep("87654-321");


        Servico servico1 = new Servico();
        servico1.setId(1);
        servico1.setDescricaoServico("Corte na régua");
        servico1.setServicoEspecial(false);
        servico1.setTempoMedioEmMinutos(20);
        servico1.setEmpresa(empresa);

        Servico servico2 = new Servico();
        servico2.setId(2);
        servico2.setDescricaoServico("Progressiva Feminina");
        servico2.setServicoEspecial(true);
        servico2.setTempoMedioEmMinutos(120);
        servico2.setEmpresa(empresa);

        servicos.add(servico1);
        servicos.add(servico2);

        return servicos;
    }

    @Test
    @DisplayName("Cenario de busca por ID de serviço")
    void buscaPorId(){
        Servico servico = setup();

        repository.save(servico);

        when(repository.findById(servico.getId())).thenReturn(Optional.of(servico));

        Servico resposta = service.buscaPorId(servico.getId());

        assertEquals(servico, resposta);
        verify(repository, times(0)).findAll();
        verify(repository, times(1)).findById(servico.getId());
    }

    @Test
    @DisplayName("Cenario de busca por Id de serviço não encontrado")
    void buscaPorIdIncorreto(){
        Integer idInformado = 1;

        when(repository.findById(idInformado)).thenReturn(Optional.empty());

        assertThrows(NaoEncontradoException.class,
        () -> service.buscaPorId(idInformado));

        verify(repository, times(0)).findAll();
        verify(repository, times(1)).findById(idInformado);
    }

    @Test
    @DisplayName("Cenario de buscar de Ids de serviços")
    void buscaTodosPorIds(){
        List<Servico> servicos = setUpLista();
        List<Integer> Ids = new ArrayList<>();
        Ids.add(1);
        Ids.add(2);

        repository.saveAll(servicos);

        when(repository.findByIdIn(Ids)).thenReturn(servicos);

        List<Servico> resposta = service.buscaTodosPorIds(Ids);

        assertEquals(servicos, resposta);
        verify(repository, times(0)).findAll();
        verify(repository, times(1)).findByIdIn(Ids);
    }

    @Test
    @DisplayName("Cenario de busca por empresa no serviço")
    void buscaPorEmpresa(){
        List<Servico> servicos = setUpLista();
        repository.saveAll(servicos);

        when(repository.findByEmpresa(servicos.get(0).getEmpresa())).thenReturn(servicos);

        List<Servico> resposta = service.buscaPorEmpresa(servicos.get(0).getEmpresa());

        assertEquals(servicos, resposta);
        verify(repository, times(0)).findAll();
        verify(repository, times(1)).findByEmpresa(servicos.get(0).getEmpresa());
    }
}