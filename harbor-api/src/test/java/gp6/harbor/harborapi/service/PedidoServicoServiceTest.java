package gp6.harbor.harborapi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import gp6.harbor.harborapi.api.enums.CargoEnum;
import gp6.harbor.harborapi.domain.cliente.Cliente;
import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.endereco.Endereco;
import gp6.harbor.harborapi.domain.pedido.Pedido;
import gp6.harbor.harborapi.domain.pedido.PedidoServico;
import gp6.harbor.harborapi.domain.pedido.repository.PedidoServicoRepository;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import gp6.harbor.harborapi.domain.servico.Servico;
import gp6.harbor.harborapi.service.pedido.PedidoServicoService;
public class PedidoServicoServiceTest {
    @Mock
    private PedidoServicoRepository repository;

    @InjectMocks
    private PedidoServicoService service;

    public PedidoServicoServiceTest(){
        MockitoAnnotations.openMocks(this);
    }

    PedidoServico setUp(){
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

        Cliente cliente = new Cliente();
        cliente.setId(1);
        cliente.setNome("Henrique");
        cliente.setSobrenome("Mosca");
        cliente.setCpf("20487594815");
        cliente.setTelefone("11985478123");


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

        Pedido pedido = new Pedido();
        pedido.setId(1);
        pedido.setFinalizado(true);
        pedido.setCliente(cliente);
        pedido.setDataAgendamento(LocalDateTime.now());
        pedido.setDataCriacao(LocalDateTime.now());
        pedido.setTotal(20.0);
        pedido.setPrestador(prestador);

        PedidoServico pedidoServico = new PedidoServico();
        pedidoServico.setPedido(pedido);
        pedidoServico.setServico(servico);

        return pedidoServico;
    }

    @Test
    @DisplayName("Cenario de salvamento de um Pedido Serviço")
    void salvar(){
        PedidoServico pedidoServico = setUp();
        Servico servico = pedidoServico.getServico();
        Pedido pedido = pedidoServico.getPedido();

        when(repository.save(pedidoServico)).thenReturn(pedidoServico);

        PedidoServico resposta = service.adicionarServico(servico, pedido);
        assertEquals(pedidoServico, resposta);
        verify(repository, times(1)).save(pedidoServico);
    }

    @Test
    @DisplayName("Cenario de salvamento de salvar todos os Pedidos Serviço")
    void salvarTodos(){
        PedidoServico pedidoServico = setUp();
        List<PedidoServico> pedidos = Arrays.asList(pedidoServico);
        
        when(repository.saveAll(pedidos)).thenReturn(pedidos);

        List<PedidoServico> resposta = service.salvarTodos(pedidos);
        assertEquals(pedidos, resposta);
        verify(repository, times(1)).saveAll(pedidos);
    }
}
