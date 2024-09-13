package gp6.harbor.harborapi.service.relatorio;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.Paragraph;
import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.pedido.PedidoPrestador;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import gp6.harbor.harborapi.domain.prestador.repository.PrestadorRepository;
import gp6.harbor.harborapi.service.pedido.PedidoService;
import gp6.harbor.harborapi.service.prestador.PrestadorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class RelatorioService {
    private final PedidoService pedidoService;
    private final PrestadorService prestadorService;
    private final PrestadorRepository prestadorRepository;

    public ByteArrayInputStream gerarRelatorio(String dados) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            // Criar o escritor e o documento PDF
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Adicionar o texto "HelloWorld"
            Paragraph paragraph = new Paragraph(dados);
            document.add(paragraph);

            // Fechar o documento
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    //criar metodo para formatar dados tem texto e depois chamando o metodo gerarRelatorio
    public ByteArrayInputStream gerarRelatorioPedidosAtendidosPorPrestador(LocalDate dataInicio, LocalDate dataFim) {
        String dados = "Relatório de Pedidos Atendidos por Prestador";
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Prestador prestadorLogado = prestadorRepository.findByEmail(emailUsuario).orElse(null);
        Empresa empresa = prestadorLogado.getEmpresa();
        if (prestadorLogado == null) {
            return gerarRelatorio("O usuário precisa estar logado");
        }

        for (Prestador prestador : prestadorRepository.findByEmpresa(empresa)) {
            int quantidadePedidos = prestadorService.buscarPedidosAtendidosPorPrestador(prestador.getId()).size();

            var lista =  prestadorService.buscarPedidosAtendidosPorPrestador(prestador.getId());

            Double totalFaturado = 0.0;
            for (PedidoPrestador pedidoPrestador : lista) {
                totalFaturado += pedidoPrestador.getPedido().getTotalPedido();
            }

            dados += "\n\n" + prestador.getNome() + ": " + "Pedidos atendidos:" + quantidadePedidos + "\nSoma dos valores: R$" + totalFaturado;
        }


        return gerarRelatorio(dados);
    }
}

