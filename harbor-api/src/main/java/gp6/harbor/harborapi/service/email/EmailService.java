package gp6.harbor.harborapi.service.email;
import gp6.harbor.harborapi.domain.pedido.PedidoPrestador;
import gp6.harbor.harborapi.domain.pedido.PedidoProduto;
import gp6.harbor.harborapi.domain.pedido.PedidoProdutoV2;
import gp6.harbor.harborapi.domain.pedido.PedidoV2;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import java.util.HashMap;


import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
    @RequiredArgsConstructor
    public class EmailService {

    private final RestTemplate restTemplate;
    private final JavaMailSender mailSender;

    private final String FLASK_API_URL = "http://127.0.0.1:5000/send-email";
    private final String FLASK_API_URL_CODE = "http://127.0.0.1:5000/send-code-email";

        @Async
        public void sendEmailV2(String to, String subject, String text) {
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
                    http://localhost:3000/avaliacao/%s
                    """, pedidoAgendado.getCliente().getNome(), pedidoAgendado.getCodigoPedido());
            return emailFormatado;
        }


    public String sendEmail(String receiverEmail, String subject, String body) {
        // Usar um Map para representar o corpo da requisição JSON
        Map<String, String> jsonRequest = new HashMap<>();
        jsonRequest.put("to", receiverEmail);
        jsonRequest.put("subject", subject);
        jsonRequest.put("body", body);

        // Configurar os headers da requisição (Content-Type: application/json)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Criar a entidade com o corpo e headers
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(jsonRequest, headers);

        // Enviar a requisição POST para o microserviço Flask
        ResponseEntity<String> response = restTemplate.exchange(
                FLASK_API_URL, HttpMethod.POST, entity, String.class);

        // Retornar a resposta da API Flask
        return response.getBody();
    }

    public String mandarEmailCodigoAcesso(String receiverEmail,String codigoAcesso, String telefoneEmpresa, String nomeEmpresa) {
        // Usar um Map para representar o corpo da requisição JSON com os novos parâmetros
        Map<String, String> jsonRequest = new HashMap<>();
        jsonRequest.put("to", receiverEmail);
        jsonRequest.put("codigo_acesso", codigoAcesso); // Adiciona o código de acesso
        jsonRequest.put("telefone_empresa", telefoneEmpresa); // Adiciona o telefone da empresa
        jsonRequest.put("nome_empresa", nomeEmpresa); // Adiciona o nome da empresa

        // Configurar os headers da requisição (Content-Type: application/json)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Criar a entidade com o corpo e headers
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(jsonRequest, headers);

        // Enviar a requisição POST para o microserviço Flask
        ResponseEntity<String> response = restTemplate.exchange(
                FLASK_API_URL_CODE, HttpMethod.POST, entity, String.class);

        // Retornar a resposta da API Flask
        return response.getBody();
    }


}