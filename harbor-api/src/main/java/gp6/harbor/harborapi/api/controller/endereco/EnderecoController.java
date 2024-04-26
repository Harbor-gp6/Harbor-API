package gp6.harbor.harborapi.api.controller.endereco;


import gp6.harbor.harborapi.domain.endereco.Endereco;
import gp6.harbor.harborapi.domain.endereco.repository.EnderecoRepository;
import gp6.harbor.harborapi.service.endereco.dto.EnderecoCriacaoDto;
import gp6.harbor.harborapi.service.endereco.dto.EnderecoListagemDto;
import gp6.harbor.harborapi.service.endereco.dto.EnderecoMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/enderecos")
public class
EnderecoController {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @PostMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<EnderecoListagemDto> cadastrar (@RequestBody @Valid EnderecoCriacaoDto novoEndereco){
        Endereco endereco = EnderecoMapper.toEntity(novoEndereco);
        Endereco enderecoSalvo = enderecoRepository.save(endereco);
        EnderecoListagemDto listagemDto = EnderecoMapper.toDto(enderecoSalvo);
        return ResponseEntity.status(201).body(listagemDto);
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<EnderecoListagemDto> buscarPeloId(@PathVariable int id){
        Optional<Endereco> enderecoOptional = enderecoRepository.findById(id);

        if (enderecoOptional.isEmpty()){
            return ResponseEntity.status(404).build();
        }

        EnderecoListagemDto dto = EnderecoMapper.toDto(enderecoOptional.get());
        return ResponseEntity.status(200).body(dto);
    }

    @GetMapping("/cep")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<EnderecoListagemDto>> buscarPeloCep(@RequestParam String cep){
        List<Endereco> enderecos = enderecoRepository.findByCep(cep);

        if (enderecos.isEmpty()){
            return ResponseEntity.status(204).build();
        }

        List<EnderecoListagemDto> listaAuxiliar = EnderecoMapper.toDto(enderecos);

        return ResponseEntity.status(200).body(listaAuxiliar);
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<EnderecoListagemDto>> buscar(){
        List<Endereco> enderecos = enderecoRepository.findAll();

        if (enderecos.isEmpty()){
            return ResponseEntity.status(204).build();
        }

        List<EnderecoListagemDto> listaAuxiliar = EnderecoMapper.toDto(enderecos);

        return ResponseEntity.status(200).body(listaAuxiliar);
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
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




    //To-Do
    //buscar por nome
    //No cadastro de Endereco, checar de o novo endereço é exatamente igual
}
