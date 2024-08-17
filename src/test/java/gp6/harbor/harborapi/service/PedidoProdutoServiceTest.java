package gp6.harbor.harborapi.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.endereco.Endereco;
import gp6.harbor.harborapi.domain.pedido.Pedido;
import gp6.harbor.harborapi.domain.pedido.PedidoProduto;
import gp6.harbor.harborapi.domain.pedido.repository.PedidoProdutoRepository;
import gp6.harbor.harborapi.domain.produto.Produto;
import gp6.harbor.harborapi.domain.servico.Servico;
import gp6.harbor.harborapi.service.pedido.PedidoProdutoService;

public class PedidoProdutoServiceTest {
    @Mock
    private PedidoProdutoRepository repository;

    @InjectMocks
    private PedidoProdutoService service;

    public PedidoProdutoServiceTest(){
        MockitoAnnotations.openMocks(this);
    }

    PedidoProduto setUp(){
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

        Produto produto = new Produto();
        produto.setId(1);
        produto.setNome("Guará Viton");
        produto.setEmpresa(empresa);
        produto.setDescricao("Suco de Açai com guaraná");
        produto.setPrecoCompra(3.0);
        produto.setPrecoVenda(5.0);

        Pedido pedido = new Pedido();
        pedido.setId(1);

        PedidoProduto pedidoProduto = new PedidoProduto();
        pedidoProduto.setProduto(produto);
        pedidoProduto.setQuantidade(1);
        pedidoProduto.setPedido(pedido);
        pedidoProduto.setQuantidade(2);

        return pedidoProduto;
    }
    @Test
    @DisplayName("Cenario de salvamento de salvar todos os Pedidos produtos")
    void salvarTodos(){
        PedidoProduto pedidoProduto = setUp();
        List<PedidoProduto> pedidos = Arrays.asList(pedidoProduto);
        
        when(repository.saveAll(pedidos)).thenReturn(pedidos);

        List<PedidoProduto> resposta = service.salvarTodos(pedidos);
        assertEquals(pedidos, resposta);
    }
}