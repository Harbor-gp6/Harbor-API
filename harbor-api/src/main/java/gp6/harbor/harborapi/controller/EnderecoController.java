package gp6.harbor.harborapi.controller;

import gp6.harbor.harborapi.dto.*;
import gp6.harbor.harborapi.entity.Endereco;
import gp6.harbor.harborapi.repository.EnderecoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @PostMapping
    public ResponseEntity<EnderecoListagemDto> cadastrar (@RequestBody @Valid EnderecoCriacaoDto novoEndereco){
        Endereco endereco = EnderecoMapper.toEntity(novoEndereco);
        Endereco enderecoSalvo = enderecoRepository.save(endereco);
        EnderecoListagemDto listagemDto = EnderecoMapper.toDto(enderecoSalvo);

        return ResponseEntity.status(201).body(listagemDto);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<EnderecoListagemDto> buscarPeloId(@PathVariable int id){
//        Optional<Endereco> enderecoOptional =
//    }

    //To-Do
    //buscar por nome
    //No cadastro de Endereco, checar de o novo endereço é exatamente igual
}
