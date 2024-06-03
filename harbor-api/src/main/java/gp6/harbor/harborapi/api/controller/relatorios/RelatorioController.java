package gp6.harbor.harborapi.api.controller.relatorios;

import gp6.harbor.harborapi.service.pedido.PedidoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/relatorios")
@RequiredArgsConstructor
public class RelatorioController {

    private final PedidoService pedidoService;

    @GetMapping("/faturamento-bruto/{prestadorId}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Double> calcularFaturamentoBruto(@RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim, @PathVariable Long prestadorId) {
        return ResponseEntity.ok(pedidoService.somarValorFaturado(dataInicio, dataFim, prestadorId));
    }

    @GetMapping("/ticket-medio/{prestadorId}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Double> calcularTicketMedio(@RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim, @PathVariable Long prestadorId) {
        return ResponseEntity.ok(pedidoService.calcularTicketMedio(dataInicio, dataFim, prestadorId));
    }

    @GetMapping("/faturamento-prestador/{prestadorId}/{prestadorBuscadoId}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Double> calcularFaturamentoPorPrestador(@RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim, @PathVariable Long prestadorId, @PathVariable Long prestadorBuscadoId) {
        return ResponseEntity.ok(pedidoService.somarValorFaturadoPorPrestador(dataInicio, dataFim, prestadorId, prestadorBuscadoId));
    }
}
