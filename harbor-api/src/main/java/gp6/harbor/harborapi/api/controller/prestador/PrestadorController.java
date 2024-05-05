package gp6.harbor.harborapi.api.controller.prestador;

import gp6.harbor.harborapi.arquivoCsv.Gravacao;
import gp6.harbor.harborapi.domain.cargo.Cargo;
import gp6.harbor.harborapi.domain.cargo.repository.CargoRepository;
import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.empresa.repository.EmpresaRepository;
import gp6.harbor.harborapi.domain.endereco.repository.EnderecoRepository;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import gp6.harbor.harborapi.domain.prestador.repository.PrestadorRepository;
import gp6.harbor.harborapi.service.prestador.dto.PrestadorCriacaoDto;
import gp6.harbor.harborapi.service.prestador.dto.PrestadorListagemDto;
import gp6.harbor.harborapi.service.prestador.dto.PrestadorMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import gp6.harbor.harborapi.service.usuario.UsuarioService;
import gp6.harbor.harborapi.service.usuario.autenticacao.dto.UsuarioLoginDto;
import gp6.harbor.harborapi.service.usuario.autenticacao.dto.UsuarioTokenDto;

import java.util.*;

@RestController
@RequestMapping("/usuarios")
public class PrestadorController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PrestadorRepository prestadorRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private CargoRepository cargoRepository;

    @PostMapping
    public ResponseEntity<Void> criar(@RequestBody @Valid PrestadorCriacaoDto usuarioCriacaoDto) {
        if (existePorCpf(usuarioCriacaoDto.getCpf())) {
            return ResponseEntity.status(409).build();
        }

        this.usuarioService.criar(usuarioCriacaoDto);

        return ResponseEntity.status(201).build();
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioTokenDto> login(@RequestBody UsuarioLoginDto usuarioLoginDto) {
        UsuarioTokenDto usuarioTokenDto = this.usuarioService.autenticar(usuarioLoginDto);

        return ResponseEntity.status(200).body(usuarioTokenDto);
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<PrestadorListagemDto>> listar(){
        List<Prestador> prestadores = prestadorRepository.findAll();

        if (prestadores.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        List<PrestadorListagemDto> listaAuxiliar = PrestadorMapper.toDto(prestadores);
        return ResponseEntity.status(200).body(listaAuxiliar);
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<PrestadorListagemDto> buscarPorId(@PathVariable long id){
        Optional<Prestador> prestadorOpt = prestadorRepository.findById(id);

        if (prestadorOpt.isEmpty()){
            return ResponseEntity.status(404).build();
        }
        PrestadorListagemDto dto = PrestadorMapper.toDto(prestadorOpt.get());
        return ResponseEntity.status(200).body(dto);
    }

    @GetMapping("/cpf")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<PrestadorListagemDto>> buscarPeloCpf(@RequestParam String cpf){
        List<Prestador> prestadores = prestadorRepository.findByCpf(cpf);

        if (prestadores.isEmpty()){
            return ResponseEntity.status(204).build();
        }

        List<PrestadorListagemDto> listaAuxiliar = PrestadorMapper.toDto(prestadores);

        return ResponseEntity.status(200).body(listaAuxiliar);
    }

    @GetMapping("/nome")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<PrestadorListagemDto>> buscarPeloNome(@RequestParam String nome){
        List<Prestador> prestadores = prestadorRepository.findByNomeContainsIgnoreCase(nome);

        if (prestadores.isEmpty()){
            return ResponseEntity.status(204).build();
        }

        List<PrestadorListagemDto> listaAuxiliar = PrestadorMapper.toDto(prestadores);

        return ResponseEntity.status(200).body(listaAuxiliar);
    }

    @GetMapping("/gerar-csv")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> gerarArquivo() {
        Gravacao.gravaArquivosCsv(prestadorRepository.findAll(), "lista-prestadores");

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<PrestadorListagemDto> atualizarPrestador(@PathVariable long id, @RequestBody @Valid PrestadorCriacaoDto prestadorAtualizado){

        if (!prestadorRepository.existsById(id)){
            return ResponseEntity.status(404).build();
        }

        Prestador prestador = PrestadorMapper.toEntity(prestadorAtualizado);
        prestador.setId(id);
        Prestador prestadorSalvo = prestadorRepository.save(prestador);
        PrestadorListagemDto listagemDto = PrestadorMapper.toDto(prestadorSalvo);

        return ResponseEntity.status(200).body(listagemDto);
    }
    public boolean existePorCpf(String cpf){
        return prestadorRepository.existsByCpf(cpf);
    }
}