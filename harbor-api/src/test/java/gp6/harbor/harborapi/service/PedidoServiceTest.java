package gp6.harbor.harborapi.service;

import gp6.harbor.harborapi.api.enums.CargoEnum;
import gp6.harbor.harborapi.domain.cliente.Cliente;
import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.endereco.Endereco;
import gp6.harbor.harborapi.domain.pedido.Pedido;
import gp6.harbor.harborapi.domain.pedido.PedidoProduto;
import gp6.harbor.harborapi.domain.pedido.PedidoServico;
import gp6.harbor.harborapi.domain.pedido.repository.PedidoRepository;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import gp6.harbor.harborapi.domain.produto.Produto;
import gp6.harbor.harborapi.domain.servico.Servico;
import gp6.harbor.harborapi.dto.pedido.dto.PedidoAtualizacaoProdutoDto;
import gp6.harbor.harborapi.service.email.EmailService;
import gp6.harbor.harborapi.service.pedido.PedidoProdutoService;
import gp6.harbor.harborapi.service.pedido.PedidoService;
import gp6.harbor.harborapi.service.pedido.PedidoServicoService;
import gp6.harbor.harborapi.service.prestador.PrestadorService;
import gp6.harbor.harborapi.service.produto.ProdutoService;
import gp6.harbor.harborapi.service.servico.ServicoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PedidoServiceTest {

    @InjectMocks
    private PedidoService pedidoService;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ProdutoService produtoService;

    @Mock
    private PedidoProdutoService pedidoProdutoService;

    @Mock
    private PedidoServicoService pedidoServicoService;

    @Mock
    private ServicoService servicoService;

    @Mock
    private PrestadorService prestadorService;

    @Mock 
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    Pedido setUpPedido() {
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
        servico.setValorServico(25.0); // Configurando valor do serviço
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
    
        PedidoServico pedidoServico = new PedidoServico();
    
        Pedido pedido = new Pedido();
        pedido.setId(1);
        pedido.setFinalizado(true);
        pedido.setCliente(cliente);
        pedido.setDataAgendamento(LocalDateTime.now());
        pedido.setDataCriacao(LocalDateTime.now());
        pedido.setTotal(20.0);
        pedido.setPrestador(prestador);
    
        pedidoServico.setPedido(pedido);
        pedidoServico.setServico(servico);
    
        List<PedidoServico> pedidoServicos = new ArrayList<>();
        pedidoServicos.add(pedidoServico);
    
        pedido.setPedidoServicos(pedidoServicos);
    
        return pedido;
    }
    
    

    @Test
    @DisplayName("Deve Adicionar Produto Ao Pedido")
    void adicionarProduto() {
        Pedido pedido = setUpPedido();
        
        Produto produto = new Produto();
        produto.setId(1);
        produto.setPrecoVenda(10.0);
        
        PedidoAtualizacaoProdutoDto.ProdutoDto produtoDto = new PedidoAtualizacaoProdutoDto.ProdutoDto();
        produtoDto.setId(1);
        produtoDto.setQuantidade(2);
    
        PedidoAtualizacaoProdutoDto produtosDto = new PedidoAtualizacaoProdutoDto();
        produtosDto.setProdutos(Collections.singletonList(produtoDto));
    
        PedidoProduto pedidoProduto = new PedidoProduto();
        pedidoProduto.setProduto(produto);
        pedidoProduto.setPedido(pedido);
        pedidoProduto.setQuantidade(2);
    
        when(pedidoRepository.findById(anyInt())).thenReturn(Optional.of(pedido));
        when(produtoService.buscarPorId(anyInt())).thenReturn(produto);
        when(pedidoProdutoService.salvarTodos(anyList())).thenReturn(Collections.singletonList(pedidoProduto));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
    
        Pedido pedidoAtualizado = pedidoService.adicionarProduto(1, produtosDto);
    
        assertNotNull(pedidoAtualizado);
        assertEquals(1, pedidoAtualizado.getPedidoProdutos().size()); // Verifique se o produto foi adicionado corretamente
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }
    
    

    @Test
    @DisplayName("Deve Retornar Lista De Pedidos")
    void listarPedidos() {
        when(pedidoRepository.findAll()).thenReturn(Collections.singletonList(new Pedido()));

        List<Pedido> pedidos = pedidoService.listarPedidos();

        assertFalse(pedidos.isEmpty());
        verify(pedidoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve Retornar Valor Faturado")
    void somarValorFaturado() {

        Prestador prestador = new Prestador();
        prestador.setId(1L);
        prestador.setCargo(CargoEnum.ADMIN); 

        when(pedidoRepository.somarFaturamentoBruto(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(1000.0);
        when(prestadorService.buscarPorId(anyLong())).thenReturn(prestador);
    

        Double valorFaturado = pedidoService.somarValorFaturado(LocalDate.now(), LocalDate.now(), 1L);

        assertEquals(1000.0, valorFaturado);
        verify(pedidoRepository, times(1)).somarFaturamentoBruto(any(LocalDateTime.class), any(LocalDateTime.class));
    }
    

    @Test
    @DisplayName("Deve Retornar Ticket Medio")
    void calcularTicketMedio() {
    
        Prestador prestador = new Prestador();
        prestador.setId(1L);
        prestador.setCargo(CargoEnum.ADMIN); 
    
        when(pedidoRepository.somarFaturamentoBruto(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(1000.0);
        when(pedidoRepository.contarPedidosPorPeriodo(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(10);
        when(prestadorService.buscarPorId(anyLong())).thenReturn(prestador); 
    
        Double ticketMedio = pedidoService.calcularTicketMedio(LocalDate.now(), LocalDate.now(), 1L);
    
        assertEquals(100.0, ticketMedio);
        verify(pedidoRepository, times(1)).somarFaturamentoBruto(any(LocalDateTime.class), any(LocalDateTime.class));
        verify(pedidoRepository, times(1)).contarPedidosPorPeriodo(any(LocalDateTime.class), any(LocalDateTime.class));
    }
    

    @Test
    @DisplayName("Deve Retornar Valor Faturado Por Prestador")
    void somarValorFaturadoPorPrestador() {
    
        Prestador prestador = new Prestador();
        prestador.setId(1L);
        prestador.setCargo(CargoEnum.ADMIN); // Configurando o cargo do prestador
    
        when(pedidoRepository.somarFaturamentoBrutoPorPrestador(any(LocalDateTime.class), any(LocalDateTime.class), anyLong())).thenReturn(1000.0);
        when(prestadorService.buscarPorId(anyLong())).thenReturn(prestador); // Usando o prestador configurado
    
        Double valorFaturado = pedidoService.somarValorFaturadoPorPrestador(LocalDate.now(), LocalDate.now(), 1L, 2L);
    
        assertEquals(1000.0, valorFaturado);
        verify(pedidoRepository, times(1)).somarFaturamentoBrutoPorPrestador(any(LocalDateTime.class), any(LocalDateTime.class), anyLong());
    }
    
}
