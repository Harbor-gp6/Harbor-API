package gp6.harbor.harborapi.controller;

import gp6.harbor.harborapi.dto.empresa.EmpresaMapper;
import gp6.harbor.harborapi.dto.prestador.PrestadorCriacaoDto;
import gp6.harbor.harborapi.dto.prestador.PrestadorListagemDto;
import gp6.harbor.harborapi.dto.prestador.PrestadorMapper;
import gp6.harbor.harborapi.entity.Empresa;
import gp6.harbor.harborapi.entity.Prestador;

import gp6.harbor.harborapi.repository.EmpresaRepository;
import gp6.harbor.harborapi.repository.EnderecoRepository;
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

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @PostMapping
    public ResponseEntity<PrestadorListagemDto> cadastrar(@RequestBody @Valid PrestadorCriacaoDto novoPrestador) {
        if (existePorCpf(novoPrestador.getCpf())) {
            return ResponseEntity.status(409).build();
        }

        // Convertendo o DTO de empresa para entidade e salvando-a
        Empresa empresa = EmpresaMapper.toEntity(novoPrestador.getEmpresa());
        empresa = empresaRepository.save(empresa);

        // Configurando a empresa para o novo prestador
        Prestador prestador = PrestadorMapper.toEntity(novoPrestador);
        prestador.setEmpresa(empresa);

        // Salvando o prestador
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
