package gp6.harbor.harborapi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.endereco.Endereco;
import gp6.harbor.harborapi.domain.produto.Produto;
import gp6.harbor.harborapi.domain.produto.repository.ProdutoRepository;
import gp6.harbor.harborapi.exception.NaoEncontradoException;
import gp6.harbor.harborapi.service.produto.ProdutoService;

public class ProdutoServiceTest {
    @Mock
    private ProdutoRepository repository;

    @InjectMocks
    private ProdutoService service;

    public ProdutoServiceTest(){
        MockitoAnnotations.openMocks(this);
    }

    Produto setUp(){
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
        
        Produto produto = new Produto();
        produto.setId(1);
        produto.setNome("Guará Viton");
        produto.setEmpresa(empresa);
        produto.setDescricao("Suco de Açai com guaraná");
        produto.setPrecoCompra(3.0);
        produto.setPrecoVenda(5.0);

        return produto;
    }

    List<Produto> setUpList(){
        List<Produto> produtos = new ArrayList<>();

        Endereco endereco = new Endereco();
        endereco.setId(1);
        endereco.setLogradouro("Rua Exemplo 1");
        endereco.setNumero("123");
        endereco.setCidade("Cidade Exemplo 1");
        endereco.setEstado("Estado Exemplo 1");
        endereco.setCep("12345-678");

        Empresa empresa = new Empresa();
        empresa.setId(1);
        empresa.setRazaoSocial("Razão Social Exemplo 1");
        empresa.setNomeFantasia("Nome Fantasia Exemplo 1");
        empresa.setCnpj("12.345.678/0001-91");
        empresa.setEndereco(endereco);
        empresa.setDataCriacao(LocalDate.now());
        empresa.setHorarioAbertura(LocalTime.of(8, 0));
        empresa.setHorarioFechamento(LocalTime.of(18, 0));
        empresa.setDataInativacao(null);

        Produto produto1 = new Produto();
        produto1.setId(1);
        produto1.setNome("Guará Viton");
        produto1.setEmpresa(empresa);
        produto1.setDescricao("Suco de Açai com guaraná");
        produto1.setPrecoCompra(3.0);
        produto1.setPrecoVenda(5.0);

        Produto produto2 = new Produto();
        produto2.setId(2);
        produto2.setNome("Coca cola Lata");
        produto2.setEmpresa(empresa);
        produto2.setDescricao("Coquinha geladinha");
        produto2.setPrecoCompra(2.0);
        produto2.setPrecoVenda(4.0);

        produtos.add(produto1);
        produtos.add(produto2);

        return produtos;
    }

    @Test
    @DisplayName("Cenario de salvamento de novos produtos")
    void cadastrarProduto(){
        Produto produto = setUp();

        when(repository.save(produto)).thenReturn(produto);

        Produto resposta = service.cadastrar(produto);
        assertEquals(produto, resposta);
    }

    @Test
    @DisplayName("Cenario de busca por Id existente")
    void buscarPorIdExistente(){
        Produto produto = setUp();
        repository.save(produto);

        when(repository.findById(produto.getId())).thenReturn(Optional.of(produto));

        Produto resposta = service.buscarPorId(produto.getId());
        assertEquals(produto, resposta);
    }

    @Test
    @DisplayName("Cenario de busca por Id inexistente")
    void buscarPorIdInexistente(){
        Integer idInformado = 1;

        assertThrows(NaoEncontradoException.class,
        () -> service.buscarPorId(idInformado));
        verify(repository, times(0)).findAll();
        verify(repository, times(1)).findById(idInformado);
    }

    @Test
    @DisplayName("Cenario de listar todos os produtos")
    void listar(){
        List<Produto> produtos = setUpList();
        repository.saveAll(produtos);

        when(repository.findAll()).thenReturn(produtos);

        List<Produto> resposta = service.listar();
        assertEquals(produtos, resposta);

        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Cenario de existir por Id")
    void existePorId(){
        Produto produto = setUp();
        repository.save(produto);

        when(repository.existsById(produto.getId())).thenReturn(true);

        Boolean resposta = service.existePorId(produto.getId());

        assertEquals(true, resposta);
    }

    @Test
    @DisplayName("Cenario de listar corretamente do maior preço de venda para o menor")
    void listarMaiorParaMenor(){
        List<Produto> produtos = setUpList();
        repository.saveAll(produtos);

        when(repository.findAllByOrderByPrecoVendaDesc()).thenReturn(produtos);

        List<Produto> resposta = service.buscarMaiorParaMenor();
        assertEquals(produtos, resposta);
        verify(repository, times(1)).findAllByOrderByPrecoVendaDesc();
    }

    @Test
    @DisplayName("Cenario de listar corretamente do menor preço de venda para o maior")
    void listarMenorParaMaior(){
        List<Produto> produtos = setUpList();
        repository.saveAll(produtos);

        List<Produto> produtosOrdenados = new ArrayList<>();
        produtosOrdenados.add(produtos.get(1));
        produtosOrdenados.add(produtos.get(0));

        when(repository.findAllByOrderByPrecoVendaAsc()).thenReturn(produtosOrdenados);

        List<Produto> resposta = service.buscarMenorParaMaior();
        assertEquals(produtosOrdenados, resposta);
        verify(repository, times(1)).findAllByOrderByPrecoVendaAsc();
    }

    @Test
    @DisplayName("Cenario de deletar executado com sucesso")
    void deletarCorretamente(){
        Produto produto = setUp();

        when(repository.findById(produto.getId())).thenReturn(Optional.of(produto));

        Produto resposta = service.deletarPorId(produto.getId());

        verify(repository, times(1)).deleteById(produto.getId());
        assertEquals(produto, resposta);
    }

    @Test
    @DisplayName("Cenário de deletar falha quando o produto não é encontrado")
    void deletarFalhaQuandoProdutoNaoEncontrado() {
        Produto produto = setUp();

        when(repository.findById(produto.getId())).thenReturn(Optional.empty());

        assertThrows(NaoEncontradoException.class, () -> {
            service.deletarPorId(produto.getId());
        });

        verify(repository, times(0)).deleteById(produto.getId());
    }

    @Test
    @DisplayName("Cenário de buscar todos por ID executado com sucesso")
    void buscarTodosPorIdCorretamente() {
        List<Produto> produtos = setUpList();
        List<Integer> ids = new ArrayList<>();
        ids.add(produtos.get(0).getId());
        ids.add(produtos.get(1).getId());

        when(repository.findByIdIn(ids)).thenReturn(produtos);

        List<Produto> resposta = service.buscarTodosPorId(ids);

        verify(repository, times(1)).findByIdIn(ids);
        assertEquals(produtos, resposta);
    }

    @Test
    @DisplayName("Cenário de buscar todos por ID com lista vazia")
    void buscarTodosPorIdComListaVazia() {
        List<Produto> produtos = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        ids.add(1);
        ids.add(2);

        when(repository.findByIdIn(ids)).thenReturn(produtos);

        List<Produto> resposta = service.buscarTodosPorId(ids);

        verify(repository, times(1)).findByIdIn(ids);
        assertTrue(resposta.isEmpty());
    }
}