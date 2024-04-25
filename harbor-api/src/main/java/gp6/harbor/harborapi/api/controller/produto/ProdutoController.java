package gp6.harbor.harborapi.api.controller.produto;

import gp6.harbor.harborapi.domain.produto.Produto;
import gp6.harbor.harborapi.domain.produto.repository.ProdutoRepository;
import gp6.harbor.harborapi.service.produto.dto.ProdutoCriacaoDto;
import gp6.harbor.harborapi.service.produto.dto.ProdutoListagemDto;
import gp6.harbor.harborapi.service.produto.dto.ProdutoMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {
    @Autowired
    private ProdutoRepository produtoRepository;

    @PostMapping
    public ResponseEntity<ProdutoListagemDto> cadastrar(@RequestBody @Valid ProdutoCriacaoDto novoProduto) {
        Produto produto = ProdutoMapper.toEntity(novoProduto);
        Produto produtoSalvo = produtoRepository.save(produto);
        ProdutoListagemDto listagemDto = ProdutoMapper.toDto(produtoSalvo);

        return ResponseEntity.status(201).body(listagemDto);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoListagemDto> buscarPeloId(@PathVariable int id){
        Optional<Produto> produtoOptional = produtoRepository.findById(id);

        if (produtoOptional.isEmpty()){
            return ResponseEntity.status(404).build();
        }

        ProdutoListagemDto dto = ProdutoMapper.toDto(produtoOptional.get());
        return ResponseEntity.status(200).body(dto);
    }
    @GetMapping
    public ResponseEntity<List<ProdutoListagemDto>> buscar(){
        List<Produto> produtos = produtoRepository.findAll();

        if (produtos.isEmpty()){
            return ResponseEntity.status(204).build();
        }

        List<ProdutoListagemDto> listaAuxiliar = ProdutoMapper.toDto(produtos);

        return ResponseEntity.status(200).body(listaAuxiliar);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoListagemDto> atualizarEndereco(
            @PathVariable int id,
            @RequestBody @Valid ProdutoCriacaoDto produtoAtualizado){

        if (!produtoRepository.existsById(id)){
            return ResponseEntity.status(404).build();
        }

        Produto produto = ProdutoMapper.toEntity(produtoAtualizado);
        produto.setId(id);
        Produto produtoSalvo = produtoRepository.save(produto);
        ProdutoListagemDto listagemDto = ProdutoMapper.toDto(produtoSalvo);

        return ResponseEntity.status(200).body(listagemDto);
    }
    @GetMapping("/maiorMenor")
    public ResponseEntity<List<ProdutoListagemDto>> buscarMaiorParaMenor(){
        List<Produto> produtos = produtoRepository.findAllByOrderByPrecoVendaDesc();

        if (produtos.isEmpty()){
            return ResponseEntity.status(204).build();
        }

        List<ProdutoListagemDto> listaAuxiliar = ProdutoMapper.toDto(produtos);

        return ResponseEntity.status(200).body(listaAuxiliar);
    }
    @GetMapping("/menorMaior")
    public ResponseEntity<List<ProdutoListagemDto>> buscarMenorParaMaior(){
        List<Produto> produtos = produtoRepository.findAllByOrderByPrecoVendaAsc();
        if (produtos.isEmpty()){
            return ResponseEntity.status(204).build();
        }

        List<ProdutoListagemDto> listaAuxiliar = ProdutoMapper.toDto(produtos);

        return ResponseEntity.status(200).body(listaAuxiliar);
    }
}
