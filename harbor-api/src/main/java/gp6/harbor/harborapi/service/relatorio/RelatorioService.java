package gp6.harbor.harborapi.service.relatorio;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.pedido.PedidoPrestador;
import gp6.harbor.harborapi.domain.pedido.PedidoProdutoV2;
import gp6.harbor.harborapi.domain.pedido.PedidoV2;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import gp6.harbor.harborapi.domain.prestador.repository.PrestadorRepository;
import gp6.harbor.harborapi.service.pedido.PedidoService;
import gp6.harbor.harborapi.service.prestador.PrestadorService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class RelatorioService {
    private final PedidoService pedidoService;
    private final PrestadorService prestadorService;
    private final PrestadorRepository prestadorRepository;


    // Método base para gerar PDF com estilo padrão
    private ByteArrayInputStream gerarRelatorioComEstilo(String dados, String titulo, Empresa empresa) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Adiciona banner no topo
            adicionarBanner(document);

            // Adiciona título
            Paragraph tituloParagrafo = new Paragraph(titulo)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(tituloParagrafo);

            // Adiciona conteúdo do relatório
            Paragraph conteudo = new Paragraph(dados);
            document.add(conteudo);

            // Adiciona rodapé com ano e nome da empresa
            adicionarRodape(document, empresa);

            // Fechar o documento
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    private void adicionarBanner(Document document) {
        try {
            // Carregar a imagem do banner a partir do classpath
            ClassPathResource resource = new ClassPathResource("images/bannerHarbor.jpg");
            String caminhoImagem = resource.getURI().toString();
            ImageData imageData = ImageDataFactory.create(caminhoImagem);
            Image bannerImage = new Image(imageData);

            // Definir largura da imagem para se ajustar à página
            bannerImage.setWidth(document.getPdfDocument().getDefaultPageSize().getWidth() - 40); // Ajusta a largura com margem
            bannerImage.setAutoScaleHeight(true); // Mantém a proporção da imagem

            // Adicionar imagem ao documento
            document.add(bannerImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void adicionarRodape(Document document, Empresa empresa) {
        Paragraph rodape = new Paragraph("Ano: " + Year.now() + " | Empresa: " + empresa.getNomeFantasia())
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(10)
                .setMarginTop(20);
        document.add(rodape);
    }
    public ByteArrayInputStream gerarRelatorioPedidosAtendidosPorPrestador(LocalDate dataInicio, LocalDate dataFim) {
        String dados = "Relatório de Pedidos Atendidos por Prestador";
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Prestador prestadorLogado = prestadorRepository.findByEmail(emailUsuario).orElse(null);
        Empresa empresa = prestadorLogado.getEmpresa();
        if (prestadorLogado == null) {
            return gerarRelatorioComEstilo("O usuário precisa estar logado", "Relatório de Pedidos Atendidos por Prestador", empresa);
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


        return gerarRelatorioComEstilo(dados, "Relatório de Pedidos Atendidos por Prestador", empresa);
    }

    public ByteArrayInputStream gerarRelatorioDeAvaliacaoPorPrestador() {
        String dados = "Relatório de Avaliação por Prestador";
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Prestador prestadorLogado = prestadorRepository.findByEmail(emailUsuario).orElse(null);
        Empresa empresa = prestadorLogado.getEmpresa();
        if (prestadorLogado == null) {
            return gerarRelatorioComEstilo("O usuário precisa estar logado", "Relatório de Avaliação por Prestador", empresa);
        }

        for (Prestador prestador : prestadorRepository.findByEmpresa(empresa)) {
            dados += "\n\n" + prestador.getNome() + ": " + "Avaliação média:" + prestador.getEstrelas();
        }

        return gerarRelatorioComEstilo(dados, "Relatório de Avaliação por Prestador", empresa);
    }

    //ver os produtos mais vendidos pela pedido produtos
    public ByteArrayInputStream gerarRelatorioProdutosMaisConsumidos(LocalDate dataInicio, LocalDate dataFim) {
        String dados = "Relatório de Produtos mais consumidos";
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Prestador prestadorLogado = prestadorRepository.findByEmail(emailUsuario).orElse(null);
        Empresa empresa = prestadorLogado.getEmpresa();

        if (prestadorLogado == null) {
            return gerarRelatorioComEstilo("O usuário precisa estar logado", "Relatório de Produtos mais consumidos", empresa);
        }

        var pedidos = pedidoService.listarPedidosV2();
        java.util.Map<String, Integer> mapaProdutos = new java.util.HashMap<>();

        for (PedidoV2 pedido : pedidos) {
            for (PedidoProdutoV2 produtoPedido : pedido.getPedidoProdutos()) {
                String nomeProduto = produtoPedido.getProduto().getNome();
                int quantidade = produtoPedido.getQuantidade();

                mapaProdutos.put(nomeProduto, mapaProdutos.getOrDefault(nomeProduto, 0) + quantidade);
            }
        }

        java.util.List<java.util.Map.Entry<String, Integer>> listaOrdenada = new java.util.ArrayList<>(mapaProdutos.entrySet());
        listaOrdenada.sort(java.util.Map.Entry.comparingByValue());

        for (int i = listaOrdenada.size() - 1; i >= 0; i--) {
            String nomeProduto = listaOrdenada.get(i).getKey();
            int quantidadeTotal = listaOrdenada.get(i).getValue();
            dados += "\n\n" + nomeProduto + ": " + quantidadeTotal + " unidades";
        }

        return gerarRelatorioComEstilo(dados, "Relatório de Produtos mais consumidos", empresa);
    }

}

