package gp6.harbor.harborapi.api.controller.prestador;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import gp6.harbor.harborapi.arquivoCsv.Gravacao;
import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import gp6.harbor.harborapi.dto.prestador.dto.PrestadorCriacaoDto;
import gp6.harbor.harborapi.dto.prestador.dto.PrestadorListagemDto;
import gp6.harbor.harborapi.dto.prestador.dto.PrestadorMapper;
import gp6.harbor.harborapi.dto.usuario.UsuarioService;
import gp6.harbor.harborapi.dto.usuario.autenticacao.dto.UsuarioLoginDto;
import gp6.harbor.harborapi.dto.usuario.autenticacao.dto.UsuarioTokenDto;
import gp6.harbor.harborapi.service.empresa.EmpresaService;
import gp6.harbor.harborapi.service.prestador.PrestadorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class PrestadorController {

    private final PrestadorService service;
    private final UsuarioService usuarioService;
    private final PrestadorService prestadorService;
    private final EmpresaService empresaService;

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
        List<Prestador> prestadores = service.listar();

        if (prestadores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<PrestadorListagemDto> dtos = PrestadorMapper.toDto(prestadores);

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<PrestadorListagemDto> buscarPorId(@PathVariable long id){
        Prestador prestador = service.buscarPorId(id);

        PrestadorListagemDto dto = PrestadorMapper.toDto(prestador);

        return ResponseEntity.status(200).body(dto);
    }

    @GetMapping("/cpf")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<PrestadorListagemDto>> buscarPeloCpf(@RequestParam String cpf){
        List<Prestador> prestadores = service.buscarPeloCpf(cpf);

        if (prestadores.isEmpty()){
            return ResponseEntity.status(204).build();
        }

        List<PrestadorListagemDto> dtos = PrestadorMapper.toDto(prestadores);

        return ResponseEntity.status(200).body(dtos);
    }

    @GetMapping("/nome")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<PrestadorListagemDto>> buscarPeloNome(@RequestParam String nome){
        List<Prestador> prestadores = service.buscarPeloNome(nome);

        if (prestadores.isEmpty()){
            return ResponseEntity.status(204).build();
        }

        List<PrestadorListagemDto> dtos = PrestadorMapper.toDto(prestadores);

        return ResponseEntity.status(200).body(dtos);
    }

    @GetMapping("/gerar-csv")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> gerarArquivo() {
        Gravacao.gravaArquivosCsv(service.listar(), "lista-prestadores");

        return ResponseEntity.ok().build();
    }

    @GetMapping("/horarios/{prestadorId}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<LocalDateTime>> listarHorariosOcupados(@PathVariable Long prestadorId) {
        List<LocalDateTime> horarios = prestadorService.listarHorariosOcupados(prestadorId);

        if (horarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(horarios);
    }

    @GetMapping("empresa/{empresaId}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<PrestadorListagemDto>> prestadorPorEmpresaId(@PathVariable Integer empresaId){
        Empresa empresaBuscada = empresaService.buscarPorId(empresaId);

        List<Prestador> prestadores = service.buscarPorEmpresa(empresaBuscada);
        if (prestadores.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        List<PrestadorListagemDto> prestadoresDto = PrestadorMapper.toDto(prestadores);
        return ResponseEntity.status(200).body(prestadoresDto);
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<PrestadorListagemDto> atualizarPrestador(@PathVariable long id, @RequestBody @Valid PrestadorCriacaoDto prestadorAtualizado){

        if (!service.existePorId(id)){
            return ResponseEntity.status(404).build();
        }

        Prestador prestador = PrestadorMapper.toEntity(prestadorAtualizado);
        prestador.setId(id);
        Prestador prestadorSalvo = prestadorService.criar(prestador);
        PrestadorListagemDto listagemDto = PrestadorMapper.toDto(prestadorSalvo);

        return ResponseEntity.status(200).body(listagemDto);
    }
    @CrossOrigin("*")
    @PatchMapping(value = "/foto/{id}", consumes = {"image/jpeg", "image/png"})
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> patchFoto(@PathVariable Long id,
                                          @RequestBody byte[] novaFoto) {
        if (!prestadorService.existePorId(id)) {
            return ResponseEntity.status(404).build();
        }

        prestadorService.setFoto(id, novaFoto);
        return ResponseEntity.status(200).build();
    }
    @GetMapping(value = "/foto/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<byte[]> getFoto(@PathVariable Long id) {
        if (!prestadorService.existePorId(id)) {
            return ResponseEntity.status(404).build();
        }

        byte[] foto = prestadorService.getFoto(id);

        return ResponseEntity.status(200).header("content-disposition",
                "attachment; filename=\"foto-cliente.jpg\"").body(foto);
    }
    public boolean existePorCpf(String cpf){
        return prestadorService.existePorCpf(cpf);
    }
}
