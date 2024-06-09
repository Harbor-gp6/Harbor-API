package gp6.harbor.harborapi.api.controller.relatorios;

import gp6.harbor.harborapi.arquivoCsv.Gravacao;
import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.service.empresa.EmpresaService;
import gp6.harbor.harborapi.service.pedido.PedidoService;
import gp6.harbor.harborapi.service.pedido.PedidoServicoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

@RestController
@RequestMapping("/relatorios")
@RequiredArgsConstructor
public class RelatorioController {

    private final PedidoService pedidoService;
    private final PedidoServicoService pedidoServicoService;
    private final EmpresaService empresaService;

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

    @GetMapping("/servicos-por-prestador/{empresaId}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Map<String, Map<String, Integer>>> getServicosPorPrestador(@PathVariable Integer empresaId, @RequestParam LocalDate dtInicio, @RequestParam LocalDate dtFim) {
        Empresa empresa = empresaService.buscarPorId(empresaId);
        Map<String, Map<String, Integer>> matriz = pedidoServicoService.criarMatrizDeServicos(empresa);
        return ResponseEntity.ok(matriz);
    }

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

    @GetMapping("/faturamento-bruto/{prestadorId}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Double> calcularFaturamentoBruto(@RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim, @PathVariable Long prestadorId) {
        Double faturamento = pedidoService.somarValorFaturado(dataInicio, dataFim, prestadorId);
        return ResponseEntity.ok(faturamento);
    }

    @GetMapping("/faturamento-bruto/csv/{prestadorId}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<String> calcularFaturamentoBrutoCSV(@RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim, @PathVariable Long prestadorId) {
        Double faturamento = pedidoService.somarValorFaturado(dataInicio, dataFim, prestadorId);
        Gravacao.gravaArquivoCsvFaturamento(faturamento, "faturamento_bruto");
        return ResponseEntity.ok("CSV gerado com sucesso");
    }


    @GetMapping("/ticket-medio/{prestadorId}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Double> calcularTicketMedio(@RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim, @PathVariable Long prestadorId) {
        Double ticketMedio = pedidoService.calcularTicketMedio(dataInicio, dataFim, prestadorId);
        return ResponseEntity.ok(ticketMedio);
    }

    @GetMapping("/ticket-medio/csv/{prestadorId}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<String> calcularTicketMedioCSV(@RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim, @PathVariable Long prestadorId) {
        Double ticketMedio = pedidoService.calcularTicketMedio(dataInicio, dataFim, prestadorId);
        Gravacao.gravaArquivoCsvTicketMedio(ticketMedio, "ticket_medio");
        return ResponseEntity.ok("CSV gerado com sucesso");
    }

    @GetMapping("/faturamento-prestador/{prestadorId}/{prestadorBuscadoId}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Double> calcularFaturamentoPorPrestador(@RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim, @PathVariable Long prestadorId, @PathVariable Long prestadorBuscadoId) {
        Double faturamentoPorPrestador = pedidoService.somarValorFaturadoPorPrestador(dataInicio, dataFim, prestadorId, prestadorBuscadoId);
        return ResponseEntity.ok(faturamentoPorPrestador);
    }

    @GetMapping("/faturamento-prestador/csv/{prestadorId}/{prestadorBuscadoId}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<String> calcularFaturamentoPorPrestadorCSV(@RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim, @PathVariable Long prestadorId, @PathVariable Long prestadorBuscadoId) {
        Double faturamentoPorPrestador = pedidoService.somarValorFaturadoPorPrestador(dataInicio, dataFim, prestadorId, prestadorBuscadoId);
        Gravacao.gravaArquivoCsvFaturamento(faturamentoPorPrestador, "faturamento_prestador_" + prestadorId + "_buscado_" + prestadorBuscadoId);
        return ResponseEntity.ok("CSV gerado com sucesso");
    }


    private void gravaArquivosCsvMatriz(Map<String, Map<String, Integer>> matriz, List<String> meses, ByteArrayOutputStream outputStream, String nomeEmpresa, String cnpj, String outrosDados) {
        try (Formatter saida = new Formatter(outputStream)) {
            // Escreve informações da empresa
            saida.format("Empresa:;%s\nCNPJ:;%s\n%s\n", nomeEmpresa, cnpj, outrosDados);

            // Escreve os meses na segunda linha
            saida.format(";"); // célula vazia para a primeira coluna (para os nomes dos funcionários)
            for (String mes : meses) {
                saida.format("%s;", mes);
            }
            saida.format("\n");

            // Escreve os dados
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


}
