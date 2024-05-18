package gp6.harbor.harborapi.api.controller.produto;

import gp6.harbor.harborapi.domain.produto.Produto;
import gp6.harbor.harborapi.domain.produto.repository.ProdutoRepository;
import gp6.harbor.harborapi.dto.produto.dto.ProdutoCriacaoDto;
import gp6.harbor.harborapi.dto.produto.dto.ProdutoListagemDto;
import gp6.harbor.harborapi.dto.produto.dto.ProdutoMapper;
import gp6.harbor.harborapi.exception.NaoEncontradoException;
import gp6.harbor.harborapi.service.produto.ProdutoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @PostMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ProdutoListagemDto> cadastrar(@RequestBody @Valid ProdutoCriacaoDto novoProduto) {
        Produto produto = ProdutoMapper.toEntity(novoProduto);
        Produto produtoSalvo = produtoService.cadastrar(produto);
        ProdutoListagemDto listagemDto = ProdutoMapper.toDto(produtoSalvo);

        return ResponseEntity.status(201).body(listagemDto);
    }
    @GetMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ProdutoListagemDto> buscarPeloId(@PathVariable int id){
        Produto produto = produtoService.buscarPorId(id);
        ProdutoListagemDto dto = ProdutoMapper.toDto(produto);
        return ResponseEntity.status(200).body(dto);
    }
    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ProdutoListagemDto>> buscar(){
        List<Produto> produtos = produtoService.listar();

        if (produtos.isEmpty()){
            return ResponseEntity.status(204).build();
        }

        List<ProdutoListagemDto> listaAuxiliar = ProdutoMapper.toDto(produtos);

        return ResponseEntity.status(200).body(listaAuxiliar);
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ProdutoListagemDto> atualizarEndereco(
            @PathVariable int id,
            @RequestBody @Valid ProdutoCriacaoDto produtoAtualizado){

        if (!produtoService.existePorId(id)){
            return ResponseEntity.status(404).build();
        }

        Produto produto = ProdutoMapper.toEntity(produtoAtualizado);
        produto.setId(id);
        Produto produtoSalvo = produtoService.cadastrar(produto);
        ProdutoListagemDto listagemDto = ProdutoMapper.toDto(produtoSalvo);

        return ResponseEntity.status(200).body(listagemDto);
    }
    @GetMapping("/maiorMenor")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ProdutoListagemDto>> buscarMaiorParaMenor(){
        List<Produto> produtos = produtoService.buscarMaiorParaMenor();

        if (produtos.isEmpty()){
            return ResponseEntity.status(204).build();
        }

        List<ProdutoListagemDto> listaAuxiliar = ProdutoMapper.toDto(produtos);

        return ResponseEntity.status(200).body(listaAuxiliar);
    }
    @GetMapping("/menorMaior")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ProdutoListagemDto>> buscarMenorParaMaior(){
        List<Produto> produtos = produtoService.buscarMenorParaMaior();
        if (produtos.isEmpty()){
            return ResponseEntity.status(204).build();
        }

        List<ProdutoListagemDto> listaAuxiliar = ProdutoMapper.toDto(produtos);

        return ResponseEntity.status(200).body(listaAuxiliar);
    }
    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> deletarPorId(@PathVariable @Valid int id) {
        try {
            produtoService.deletarPorId(id);
            return ResponseEntity.status(204).build();
        } catch (NaoEncontradoException exception) {
            return ResponseEntity.status(404).build();
        }
    }

}
