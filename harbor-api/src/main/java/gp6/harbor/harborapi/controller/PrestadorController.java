package gp6.harbor.harborapi.controller;

import gp6.harbor.harborapi.dto.*;
import gp6.harbor.harborapi.entity.Prestador;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import gp6.harbor.harborapi.repository.PrestadorRepository;

import java.util.List;

@RestController
@RequestMapping("/prestadores")
public class PrestadorController {

    @Autowired
    private PrestadorRepository prestadorRepository;

    @PostMapping
    public ResponseEntity<PrestadorListagemDto> cadastrar(@RequestBody @Valid PrestadorCriacaoDto novoPrestador){
        if (existePorCpf(novoPrestador.getCpf())){
            return ResponseEntity.status(409).build();
        }

        Prestador prestador = PrestadorMapper.toEntity(novoPrestador);
        Prestador prestadorSalvo = prestadorRepository.save(prestador);
        PrestadorListagemDto listagemDto = PrestadorMapper.toDto(prestadorSalvo);

        return ResponseEntity.status(201).body(listagemDto);
    }
    @GetMapping
    public ResponseEntity<List<PrestadorListagemDto>> listar(){
        List<Prestador> prestadores = prestadorRepository.findAll();

        if (prestadores.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        List<PrestadorListagemDto> listaAuxiliar = PrestadorMapper.toDto(prestadores);
        return ResponseEntity.status(200).body(listaAuxiliar);
    }
    public boolean existePorCpf(String cpf){
        return prestadorRepository.existsByCpf(cpf);
    }
}
