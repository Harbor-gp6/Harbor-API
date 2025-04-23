package gp6.harbor.harborapi.api.controller.prestador;

import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.pedido.HorarioOcupadoDTO;
import gp6.harbor.harborapi.domain.pedido.repository.HorarioOcupado;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import gp6.harbor.harborapi.dto.prestador.dto.*;
import gp6.harbor.harborapi.dto.usuarioPrestador.UsuarioPrestadorService;
import gp6.harbor.harborapi.dto.usuarioPrestador.autenticacao.dto.UsuarioPrestadorLoginDto;
import gp6.harbor.harborapi.dto.usuarioPrestador.autenticacao.dto.UsuarioPrestadorTokenDto;
import gp6.harbor.harborapi.service.empresa.EmpresaService;
import gp6.harbor.harborapi.service.prestador.AvaliacaoPrestadorService;
import gp6.harbor.harborapi.service.prestador.PrestadorService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/prestador")
@RequiredArgsConstructor
public class PrestadorController {

    private final EmpresaService empresaService;
    private final UsuarioPrestadorService usuarioPrestadorService;
    private final PrestadorService prestadorService;
    private final AvaliacaoPrestadorService avaliacaoPrestadorService;

    @GetMapping("/horariosOcupados/{prestadorId}")
    public ResponseEntity<List<HorarioOcupadoDTO>> listarHorariosOcupadosPrestador(@PathVariable Long prestadorId) {

        List<HorarioOcupadoDTO> horariosOcupados = prestadorService.listarHorariosOcupadosPrestador(prestadorId);
        if (horariosOcupados.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(horariosOcupados);
    }

    @PostMapping
    public ResponseEntity<Void> criar(@RequestBody @Valid PrestadorCriacaoDto usuarioPrestadorCriacaoDto) {
        if (existePorCpf(usuarioPrestadorCriacaoDto.getCpf())) {
            return ResponseEntity.status(409).build();
        }

        this.usuarioPrestadorService.criar(usuarioPrestadorCriacaoDto);

        return ResponseEntity.status(201).build();
    }

    @PostMapping("criar-funcionario")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> criarFuncionario(@RequestBody @Valid PrestadorFuncionarioCriacao novoPrestadorDto) {
        if (existePorCpf(novoPrestadorDto.getCpf())) {
            return ResponseEntity.status(409).build();
        }

        usuarioPrestadorService.criarFuncionario(novoPrestadorDto);

        return ResponseEntity.status(201).build();
    }

    @GetMapping("obter-funcionarios")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<FuncionarioListagemDto>> listarFuncionarios() {
        List<FuncionarioListagemDto> funcionarios = prestadorService.listarFuncionarios();

        if (funcionarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(funcionarios);
    }

    @PatchMapping("/{id}/inativar")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> inativarPrestador(@PathVariable Long id) {
        if (!prestadorService.existePorId(id)) {
            return ResponseEntity.status(404).build();
        }

        prestadorService.inativarPrestador(id);
        return ResponseEntity.status(200).build();
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioPrestadorTokenDto> login(@RequestBody UsuarioPrestadorLoginDto usuarioPrestadorLoginDto) {
        UsuarioPrestadorTokenDto usuarioPrestadorTokenDto = this.usuarioPrestadorService.autenticar(usuarioPrestadorLoginDto);

        return ResponseEntity.status(200).body(usuarioPrestadorTokenDto);
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<PrestadorFuncionarioCriacao>> listar() {
        List<PrestadorFuncionarioCriacao> prestadores = prestadorService.listar();

        if (prestadores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(prestadores);
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<PrestadorListagemDto> buscarPorId(@PathVariable long id) {
        Prestador prestador = prestadorService.buscarPorId(id);

        PrestadorListagemDto dto = PrestadorMapper.toDto(prestador);

        return ResponseEntity.status(200).body(dto);
    }

    @GetMapping("/cpf")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<PrestadorListagemDto>> buscarPeloCpf(@RequestParam String cpf) {
        List<Prestador> prestadores = prestadorService.buscarPeloCpf(cpf);

        if (prestadores.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        List<PrestadorListagemDto> dtos = PrestadorMapper.toDto(prestadores);

        return ResponseEntity.status(200).body(dtos);
    }

    @GetMapping("/nome")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<PrestadorListagemDto>> buscarPeloNome(@RequestParam String nome) {
        List<Prestador> prestadores = prestadorService.buscarPeloNome(nome);

        if (prestadores.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        List<PrestadorListagemDto> dtos = PrestadorMapper.toDto(prestadores);

        return ResponseEntity.status(200).body(dtos);
    }

    @GetMapping("/empresa/{empresaId}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<PrestadorListagemDto>> prestadorPorEmpresaId(@PathVariable Integer empresaId) {
        Empresa empresaBuscada = empresaService.buscarPorId(empresaId);

        List<Prestador> prestadores = prestadorService.buscarPorEmpresa(empresaBuscada);
        if (prestadores.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        List<PrestadorListagemDto> prestadoresDto = PrestadorMapper.toDto(prestadores);
        return ResponseEntity.status(200).body(prestadoresDto);
    }

    @PutMapping("/{cpf}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<PrestadorFuncionarioCriacao> atualizarPrestador(
            @RequestBody @Valid PrestadorFuncionarioCriacao prestadorDto, @PathVariable String cpf) {

        if (!prestadorService.existePorCpf(cpf)) {
            return ResponseEntity.status(404).build();
        }

        PrestadorFuncionarioCriacao prestadorSalvo = usuarioPrestadorService.atualizarFuncionario(prestadorDto, cpf);

        return ResponseEntity.status(200).body(prestadorSalvo);
    }

    @PutMapping("/perfil/{cpf}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<PrestadorFuncionarioCriacao> atualizarPrestadorSemSenha(
            @RequestBody @Valid PrestadorEdicaoDTO prestadorDto, @PathVariable String cpf) {

        if (!prestadorService.existePorCpf(cpf)) {
            return ResponseEntity.status(404).build();
        }

        PrestadorFuncionarioCriacao prestadorSalvo = usuarioPrestadorService.atualizarFuncionarioSemSenha(prestadorDto, cpf);

        return ResponseEntity.status(200).body(prestadorSalvo);
    }

    @CrossOrigin("*")
    @PatchMapping(value = "/foto/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> atualizarFoto(@PathVariable Long id,
            @RequestBody String novaFoto) {
        if (!prestadorService.existePorId(id)) {
            return ResponseEntity.status(404).build();
        }

        prestadorService.setFoto(id, novaFoto);
        return ResponseEntity.status(200).build();
    }

    @Hidden
    @GetMapping(value = "/foto/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<String> getFoto(@PathVariable Long id) {
        if (!prestadorService.existePorId(id)) {
            return ResponseEntity.status(404).build();
        }

        String foto = prestadorService.getFoto(id);

        return ResponseEntity.status(200).body(foto);
    }

    @PostMapping("/enviar-codigo-acesso")
    public ResponseEntity<String> gerarCodigoAcesso(@RequestParam String email) {
        prestadorService.EnviarCodigoAcesso(email);
        return ResponseEntity.status(200).body("Se o email existe, foi enviado o código de acesso");
    }

    @PostMapping("/atualizar-senha-prestador")
    public ResponseEntity<Void> atualizarSenha(@RequestParam String email, String codigoAcesso, String novaSenha) {
        prestadorService.atualizarSenha(email, codigoAcesso, novaSenha);
        return ResponseEntity.status(200).build();
    }

    public boolean existePorCpf(String cpf) {
        return prestadorService.existePorCpf(cpf);
    }
}
