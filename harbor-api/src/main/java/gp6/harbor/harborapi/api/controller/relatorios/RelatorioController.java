package gp6.harbor.harborapi.api.controller.relatorios;

import java.util.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.TextStyle;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import java.io.ByteArrayInputStream;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Hidden;
import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.arquivoCsv.Gravacao;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import gp6.harbor.harborapi.service.empresa.EmpresaService;
import gp6.harbor.harborapi.service.pedido.PedidoService;
import gp6.harbor.harborapi.service.relatorio.RelatorioService;
import org.springframework.web.bind.annotation.RestController;
import gp6.harbor.harborapi.service.pedido.PedidoServicoService;
import java.io.ByteArrayOutputStream;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/relatorios")
@RequiredArgsConstructor
public class RelatorioController {

    private final PedidoService pedidoService;
    private final EmpresaService empresaService;
    private final RelatorioService relatorioService;
    private final PedidoServicoService pedidoServicoService;
    @Hidden
    private List<String> gerarListaDeMeses(LocalDate dataInicio, LocalDate dataFim) {
        List<String> meses = new ArrayList<>();
        LocalDate dataAtual = dataInicio.withDayOfMonth(1);

        while (!dataAtual.isAfter(dataFim)) {
            String nomeMes = dataAtual.getMonth().getDisplayName(TextStyle.FULL, new Locale("pt", "BR"));
            meses.add(nomeMes);
            dataAtual = dataAtual.plusMonths(1);
        }

        return meses;
    }
    @Hidden
    @GetMapping("/servicos-por-prestador/{empresaId}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Map<String, Map<String, Integer>>> getServicosPorPrestador(@PathVariable Integer empresaId, @RequestParam LocalDate dtInicio, @RequestParam LocalDate dtFim) {
        Empresa empresa = empresaService.buscarPorId(empresaId);
        Map<String, Map<String, Integer>> matriz = pedidoServicoService.criarMatrizDeServicos(empresa);
        return ResponseEntity.ok(matriz);
    }
    @Hidden
    @GetMapping("/servicos-por-prestador/csv/{empresaId}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<byte[]> getServicosPorPrestadorCSV(@PathVariable Integer empresaId,
                                                             @RequestParam LocalDate dtInicio,
                                                             @RequestParam LocalDate dtFim) throws IOException {
        Empresa empresa = empresaService.buscarPorId(empresaId);
        Map<String, Map<String, Integer>> matriz = pedidoServicoService.criarMatrizDeServicos(empresa);
        String nomeEmpresa = empresa.getNomeFantasia();
        String cnpj = empresa.getCnpj();
        String outrosDados = "Endereço: " + empresa.getEndereco();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        gravaArquivosCsvMatriz(matriz, gerarListaDeMeses(dtInicio, dtFim), outputStream, nomeEmpresa, cnpj, outrosDados);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=servicos_por_prestador.csv");
        headers.setContentType(MediaType.TEXT_PLAIN);

        return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
    }
    @Hidden
    @GetMapping("/faturamento-bruto/{prestadorId}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Double> calcularFaturamentoBruto(@RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim, @PathVariable Long prestadorId) {
        Double faturamento = pedidoService.somarValorFaturado(dataInicio, dataFim, prestadorId);
        return ResponseEntity.ok(faturamento);
    }
    @Hidden
    @GetMapping("/faturamento-bruto/csv/{prestadorId}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<String> calcularFaturamentoBrutoCSV(@RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim, @PathVariable Long prestadorId) {
        Double faturamento = pedidoService.somarValorFaturado(dataInicio, dataFim, prestadorId);
        Gravacao.gravaArquivoCsvFaturamento(faturamento, "faturamento_bruto");
        return ResponseEntity.ok("CSV gerado com sucesso");
    }

    @Hidden
    @GetMapping("/ticket-medio/{prestadorId}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Double> calcularTicketMedio(@RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim, @PathVariable Long prestadorId) {
        Double ticketMedio = pedidoService.calcularTicketMedio(dataInicio, dataFim, prestadorId);
        return ResponseEntity.ok(ticketMedio);
    }

    @Hidden
    @GetMapping("/ticket-medio/csv/{prestadorId}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<String> calcularTicketMedioCSV(@RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim, @PathVariable Long prestadorId) {
        Double ticketMedio = pedidoService.calcularTicketMedio(dataInicio, dataFim, prestadorId);
        Gravacao.gravaArquivoCsvTicketMedio(ticketMedio, "ticket_medio");
        return ResponseEntity.ok("CSV gerado com sucesso");
    }


    @Hidden
    @GetMapping("/faturamento-prestador/csv/{prestadorId}/{prestadorBuscadoId}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<String> calcularFaturamentoPorPrestadorCSV(@RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim, @PathVariable Long prestadorId, @PathVariable Long prestadorBuscadoId) {
        Double faturamentoPorPrestador = pedidoService.somarValorFaturadoPorPrestador(dataInicio, dataFim, prestadorId, prestadorBuscadoId);
        Gravacao.gravaArquivoCsvFaturamento(faturamentoPorPrestador, "faturamento_prestador_" + prestadorId + "_buscado_" + prestadorBuscadoId);
        return ResponseEntity.ok("CSV gerado com sucesso");
    }

    @Hidden
    private void gravaArquivosCsvMatriz(Map<String, Map<String, Integer>> matriz, List<String> meses, ByteArrayOutputStream outputStream, String nomeEmpresa, String cnpj, String outrosDados) {
        try (Formatter saida = new Formatter(outputStream)) {
            saida.format("Empresa:;%s\nCNPJ:;%s\n%s\n", nomeEmpresa, cnpj, outrosDados);

            saida.format(";"); // célula vazia para a primeira coluna (para os nomes dos funcionários)
            for (String mes : meses) {
                saida.format("%s;", mes);
            }
            saida.format("\n");

            for (Map.Entry<String, Map<String, Integer>> entry : matriz.entrySet()) {
                String funcionario = entry.getKey();
                Map<String, Integer> mapaMes = entry.getValue();

                saida.format("%s;", funcionario);

                for (String mes : meses) {
                    Integer valor = mapaMes.getOrDefault(mes, 0);
                    saida.format("%d;", valor);
                }
                saida.format("\n");
            }
        } catch (FormatterClosedException erro) {
            throw new RuntimeException("Erro ao gravar o arquivo", erro);
        }
    }

    @GetMapping("/PDF/pedidos-atendidos-por-prestador")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<byte[]> downloadRelatorio( @RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim) {
        ByteArrayInputStream bis = relatorioService.gerarRelatorioPedidosAtendidosPorPrestador(dataInicio, dataFim);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=Pedidos atendidos por prestador.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(bis.readAllBytes());
    }

    @GetMapping("/PDF/avaliacao-por-prestador")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<byte[]> downloadRelatorioDeAvaliacaoPorPrestador() {
        ByteArrayInputStream bis = relatorioService.gerarRelatorioDeAvaliacaoPorPrestador();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=Avaliacao por prestador.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(bis.readAllBytes());
    }

    @GetMapping("/PDF/produtos-mais-consumidos")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<byte[]> downloadRelatorioProdutosMaisConsumidos(@RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim) {
        ByteArrayInputStream bis = relatorioService.gerarRelatorioProdutosMaisConsumidos(dataInicio, dataFim);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=Produtos mais consumidos.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(bis.readAllBytes());
    }

    @GetMapping("/faturamento-empresa")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Double> calcularFaturamentoEmpresa(@RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim) {
        Double faturamentoPorEmpresa = pedidoService.somarValorFaturado(dataInicio, dataFim);
        return ResponseEntity.ok(faturamentoPorEmpresa);
    }
    @GetMapping("/pedidos-por-forma-pagamento")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<Object[]>> listarPedidosPorFormaPagamento(@RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim) {
        List<Object[]> pedidosPorFormaPagamento =  pedidoService.PedidosPorFormaPagamento(dataInicio, dataFim);
        return ResponseEntity.ok(pedidosPorFormaPagamento);
    }

}
