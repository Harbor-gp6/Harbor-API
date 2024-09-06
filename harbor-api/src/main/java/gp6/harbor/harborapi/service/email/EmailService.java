package gp6.harbor.harborapi.service.email;
import gp6.harbor.harborapi.domain.pedido.PedidoPrestador;
import gp6.harbor.harborapi.domain.pedido.PedidoProduto;
import gp6.harbor.harborapi.domain.pedido.PedidoProdutoV2;
import gp6.harbor.harborapi.domain.pedido.PedidoV2;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
    @RequiredArgsConstructor
    public class EmailService {

        private final JavaMailSender mailSender;

        @Async
        public void sendEmail(String to, String subject, String text) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            message.setFrom("harborApi@outlook.com");

            mailSender.send(message);
        }

        //formatar email
        public String formatarEmail(String to, String subject, PedidoV2 pedidoAgendado) {
            List<PedidoPrestador> pedidoPrestador = pedidoAgendado.getPedidoPrestador();
            List<PedidoProdutoV2> pedidoProduto = pedidoAgendado.getPedidoProdutos();
            String emailFormatado = String.format(
                    """
                    Olá, %s!
                    Seu pedido foi realizado com sucesso!
                    
                    Detalhes do pedido:
                    """, pedidoAgendado.getCliente().getNome());
            for (PedidoPrestador pedido : pedidoPrestador) {
                String servico = String.format("Serviço: %s prestado por %s\n", pedido.getServico().getDescricaoServico(), pedido.getPrestador().getNome());
                String valor = String.format("Valor: R$ %.2f\n", pedido.getServico().getValorServico());
                emailFormatado += servico + valor;
            }

            emailFormatado += "\n";

            for (PedidoProdutoV2 produto : pedidoProduto) {
                String produtoNome = String.format("Produto: %s\n", produto.getProduto().getNome());
                String produtoValor = String.format("Valor: R$ %.2f\n", produto.getProduto().getPrecoVenda());
                emailFormatado += produtoNome + produtoValor;
            }

            emailFormatado += "\n";

            emailFormatado += String.format("""
                    Total: R$ %.2f
                    
                    Agendado para: %s
                    """, pedidoAgendado.getTotalPedido(), pedidoAgendado.getDataAgendamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

            emailFormatado += String.format("""
                    Compareça ao local do serviço com 15 minutos de antecedência.
                    """);

            return emailFormatado;
        }


        //formatar email em html
        public String formatarEmailHtml(PedidoV2 pedidoAgendado) {
            List<PedidoPrestador> pedidoPrestador = pedidoAgendado.getPedidoPrestador();
            List<PedidoProdutoV2> pedidoProduto = pedidoAgendado.getPedidoProdutos();
            String emailFormatado = String.format(
                    """
                    <h1>Olá, %s!</h1>
                    <p>Seu pedido foi realizado com sucesso!</p>
                    
                    <h2>Detalhes do pedido:</h2>
                    """, pedidoAgendado.getCliente().getNome());
            for (PedidoPrestador pedido : pedidoPrestador) {
                String servico = String.format("<p>Serviço: %s prestado por %s</p>", pedido.getServico().getDescricaoServico(), pedido.getPrestador().getNome());
                String valor = String.format("<p>Valor: R$ %.2f</p>", pedido.getServico().getValorServico());
                emailFormatado += servico + valor;
            }

            emailFormatado += "<br>";

            for (PedidoProdutoV2 produto : pedidoProduto) {
                String produtoNome = String.format("<p>Produto: %s</p>", produto.getProduto().getNome());
                String produtoValor = String.format("<p>Valor: R$ %.2f</p>", produto.getProduto().getPrecoVenda());
                emailFormatado += produtoNome + produtoValor;
            }

            emailFormatado += "<br>";

            emailFormatado += String.format("""
                    <p>Total: R$ %.2f</p>
                    
                    <p>Agendado para: %s</p>
                    """, pedidoAgendado.getTotalPedido(), pedidoAgendado.getDataAgendamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

            emailFormatado += String.format("""
                    <p>Compareça ao local do serviço com 15 minutos de antecedência.</p>
                    """);

            return emailFormatado;
        }

        //formatar email pedido finalizado pedir para avaliar os prestadores
        public String formatarEmailPedidoFinalizado(PedidoV2 pedidoAgendado) {
            List<PedidoPrestador> pedidoPrestador = pedidoAgendado.getPedidoPrestador();
            List<PedidoProdutoV2> pedidoProduto = pedidoAgendado.getPedidoProdutos();
            String emailFormatado = String.format(
                    """
                    Olá, %s!
                    Seu pedido foi finalizado com sucesso!
                    
                    Por gentileza, avalie o pedido em:
                    http://localhost:8080/rating/%s
                    """, pedidoAgendado.getCliente().getNome(), pedidoAgendado.getCodigoPedido());
            return emailFormatado;
        }

    }