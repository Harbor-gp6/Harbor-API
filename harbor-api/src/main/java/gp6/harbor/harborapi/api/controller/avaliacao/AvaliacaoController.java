package gp6.harbor.harborapi.api.controller.avaliacao;

import gp6.harbor.harborapi.dto.prestador.dto.AvaliacaoPrestadorDto;
import gp6.harbor.harborapi.service.prestador.AvaliacaoPrestadorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/avaliacoes")
@RequiredArgsConstructor
public class AvaliacaoController {
    private final AvaliacaoPrestadorService avaliacaoPrestadorService;

    @PostMapping("/avaliar")
    public ResponseEntity<Void> avaliarPrestador(@RequestBody @Valid AvaliacaoPrestadorDto avaliacaoPrestadorDto){
        avaliacaoPrestadorService.criarAvaliacaoPrestador(avaliacaoPrestadorDto);
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/prestador/{idPrestador}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<?> buscarAvaliacaoPorPrestador(@PathVariable Long idPrestador){
        return ResponseEntity.ok(avaliacaoPrestadorService.buscarAvaliacaoPorPrestador(idPrestador));
    }

    @GetMapping("/cliente/{cpfCliente}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<?> buscarAvaliacaoPorCliente(@PathVariable String cpfCliente){
        return ResponseEntity.ok(avaliacaoPrestadorService.buscarAvaliacaoPorCliente(cpfCliente));
    }

    @GetMapping("/empresa")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<?> buscarAvaliacaoPorEmpresa(){
        return ResponseEntity.ok(avaliacaoPrestadorService.buscarTodasAvaliacoes());
    }


    @GetMapping("/avaliacoes-prestador")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<?> buscarAvaliacaoPrestador(){
        return ResponseEntity.ok(avaliacaoPrestadorService.buscarAvaliacaoPrestador());
    }

    @GetMapping("/media-prestador")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<?> buscarAvaliacaoMediaPrestador(){
        return ResponseEntity.ok(avaliacaoPrestadorService.buscarAvaliacaoMediaPrestador());
    }






}
