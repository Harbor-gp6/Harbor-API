package gp6.harbor.harborapi.controller;

import gp6.harbor.harborapi.dto.*;
import gp6.harbor.harborapi.entity.Endereco;
import gp6.harbor.harborapi.repository.EnderecoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @PostMapping
    public ResponseEntity<EnderecoListagemDto> cadastrar (@RequestBody @Valid EnderecoCriacaoDto novoEndereco){
        if (enderecoRepository.existsByRuaAndNumeroAndCidadeAndEstadoAndCep(
                novoEndereco.getBairro(),
                novoEndereco.getLogradouro(),
                novoEndereco.getCidade(),
                novoEndereco.getEstado(),
                novoEndereco.getNumero(),
                novoEndereco.getCep(),
                novoEndereco.getComplemento())) {
            return ResponseEntity.status(404).build();
        }
        Endereco endereco = EnderecoMapper.toEntity(novoEndereco);
        Endereco enderecoSalvo = enderecoRepository.save(endereco);
        EnderecoListagemDto listagemDto = EnderecoMapper.toDto(enderecoSalvo);

        return ResponseEntity.status(201).body(listagemDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnderecoListagemDto> buscarPeloId(@PathVariable int id){
        Optional<Endereco> enderecoOptional = enderecoRepository.findById(id);

        if (enderecoOptional.isEmpty()){
            return ResponseEntity.status(404).build();
        }

        EnderecoListagemDto dto = EnderecoMapper.toDto(enderecoOptional.get());
        return ResponseEntity.status(200).body(dto);
    }

    @GetMapping("/cep")
    public ResponseEntity<List<EnderecoListagemDto>> buscarPeloCep(@RequestParam String cep){
        List<Endereco> enderecos = enderecoRepository.findByCep(cep);

        if (enderecos.isEmpty()){
            return ResponseEntity.status(204).build();
        }

        List<EnderecoListagemDto> listaAuxiliar = EnderecoMapper.toDto(enderecos);

        return ResponseEntity.status(200).body(listaAuxiliar);
    }

    @GetMapping
    public ResponseEntity<List<EnderecoListagemDto>> buscar(){
        List<Endereco> enderecos = enderecoRepository.findAll();

        if (enderecos.isEmpty()){
            return ResponseEntity.status(204).build();
        }

        List<EnderecoListagemDto> listaAuxiliar = EnderecoMapper.toDto(enderecos);

        return ResponseEntity.status(200).body(listaAuxiliar);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnderecoListagemDto> atualizarEndereco(
            @PathVariable int id,
            @RequestBody @Valid EnderecoCriacaoDto enderecoAtualizado){

        if (!enderecoRepository.existsById(id)){
            return ResponseEntity.status(404).build();
        }

        Endereco endereco = EnderecoMapper.toEntity(enderecoAtualizado);
        endereco.setId(id);
        Endereco enderecoSalvo = enderecoRepository.save(endereco);
        EnderecoListagemDto listagemDto = EnderecoMapper.toDto(enderecoSalvo);

        return ResponseEntity.status(200).body(listagemDto);
    }
    @GetMapping("/nome")
    public ResponseEntity<List<EnderecoListagemDto>> buscarPorNome(@RequestParam String nome) {
        List<Endereco> enderecos = enderecoRepository.findByNome(nome);

        if (enderecos.isEmpty()) {
            return ResponseEntity.status(404).build();
        }

        List<EnderecoListagemDto> listaAuxiliar = EnderecoMapper.toDto(enderecos);

        return ResponseEntity.status(200).body(listaAuxiliar);
    }



    //To-Do
    //buscar por nome
    //No cadastro de Endereco, checar de o novo endereço é exatamente igual
}
