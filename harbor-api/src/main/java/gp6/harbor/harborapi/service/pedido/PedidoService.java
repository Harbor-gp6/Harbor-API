package gp6.harbor.harborapi.service.pedido;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import gp6.harbor.harborapi.api.controller.pedido.PedidoController;
import gp6.harbor.harborapi.domain.pedido.Pedido;
import gp6.harbor.harborapi.domain.pedido.PedidoProduto;
import gp6.harbor.harborapi.domain.pedido.PedidoServico;
import gp6.harbor.harborapi.domain.pedido.repository.PedidoRepository;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import gp6.harbor.harborapi.domain.produto.Produto;
import gp6.harbor.harborapi.domain.servico.Servico;
import gp6.harbor.harborapi.dto.pedido.dto.PedidoAtualizacaoProdutoDto;
import gp6.harbor.harborapi.dto.pedido.dto.PedidoAtualizacaoStatusDto;
import gp6.harbor.harborapi.exception.NaoEncontradoException;
import gp6.harbor.harborapi.exception.PedidoCapacidadeExcedidoException;
import gp6.harbor.harborapi.service.email.EmailService;
import gp6.harbor.harborapi.service.prestador.PrestadorService;
import gp6.harbor.harborapi.service.produto.ProdutoService;
import gp6.harbor.harborapi.service.servico.ServicoService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoService produtoService;
    private final PedidoProdutoService pedidoProdutoService;
    private final PedidoServicoService pedidoServicoService;
    private final ServicoService servicoService;
    private final PrestadorService prestadorService;
    private final EmailService emailService;

    public Pedido criarPedido(Pedido novoPedido, List<Integer> servicosIds) {
        if (novoPedido.getDataAgendamento().getHour() < novoPedido.getPrestador().getEmpresa().getHorarioAbertura().getHour() || novoPedido.getDataAgendamento().getHour() > novoPedido.getPrestador().getEmpresa().getHorarioFechamento().getHour()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (PedidoController.filaPedido.filaCheia()) {
            throw new PedidoCapacidadeExcedidoException("Pedidos demais, aguarde um momento");
        }


        Pedido pedido = pedidoRepository.save(novoPedido);

        AtomicReference<Double> total = new AtomicReference<>(0.0);

        List<Servico> servicosDoBanco = servicoService.buscaTodosPorIds(servicosIds);

        List<PedidoServico> listPedidoServico = new ArrayList<>();



        servicosDoBanco.forEach(servico -> {
            PedidoServico pedidoServico = new PedidoServico();

            pedidoServico.setPedido(pedido);
            pedidoServico.setServico(servico);

            total.updateAndGet(v -> v + servico.getValorServico());

            listPedidoServico.add(pedidoServico);
        });

        pedido.setTotal(total.get());

        pedido.setPedidoServicos(pedidoServicoService.salvarTodos(listPedidoServico));

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        // String subject = "AGENDAMENTO REALIZADO COM SUCESSO";
        // String message = "Voce tem um agendamento em " + pedidoSalvo.getPrestador().getEmpresa().getNomeFantasia() + " para dia " + pedidoSalvo.getDataAgendamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " às " + pedidoSalvo.getDataAgendamento().format(DateTimeFormatter.ofPattern("HH:mm")) + " com " + pedidoSalvo.getPrestador().getNome() + " " + pedidoSalvo.getPrestador().getSobrenome();

        // emailService.sendEmail(pedidoSalvo.getCliente().getEmail(), subject, message);

        // subject = "VOCÊ TEM UM NOVO SERVIÇO AGENDADO";
        // message = "Voce tem um serviço agendado para dia " + pedidoSalvo.getDataAgendamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " às " + pedidoSalvo.getDataAgendamento().format(DateTimeFormatter.ofPattern("HH:mm")) + " com cliente " + pedidoSalvo.getCliente().getNome() + " " + pedidoSalvo.getCliente().getSobrenome();

        // emailService.sendEmail(pedidoSalvo.getPrestador().getEmail(), subject, message);

        PedidoController.filaPedido.adicionarPedido(pedidoSalvo);

        return pedidoSalvo;
    }

    public Pedido adicionarProduto(Integer pedidoId, PedidoAtualizacaoProdutoDto produtos) {
        Pedido pedidoEncontrado = pedidoRepository.findById(pedidoId).orElseThrow(() -> new NaoEncontradoException("Pedido"));

        List<PedidoProduto> pedidoProdutos = new ArrayList<>();

        AtomicReference<Double> total = new AtomicReference<>(pedidoEncontrado.getTotal());

        produtos.getProdutos().forEach(produtoDto -> {
            Produto produto = produtoService.buscarPorId(produtoDto.getId());
            PedidoProduto pedidoProduto = new PedidoProduto();

            pedidoProduto.setQuantidade(produtoDto.getQuantidade());
            pedidoProduto.setProduto(produto);
            pedidoProduto.setPedido(pedidoEncontrado);

            total.updateAndGet(v -> v + produto.getPrecoVenda() * produtoDto.getQuantidade());

            pedidoProdutos.add(pedidoProduto);
        });

        List<PedidoProduto> salvo = pedidoProdutoService.salvarTodos(pedidoProdutos);

        pedidoEncontrado.setPedidoProdutos(salvo);

        return pedidoRepository.save(pedidoEncontrado);
    }

    public Pedido atualizarStatus(Integer pedidoId, PedidoAtualizacaoStatusDto status) {
        Pedido pedidoEncontrado = pedidoRepository.findById(pedidoId).orElseThrow(() -> new NaoEncontradoException("Pedido"));

        pedidoEncontrado.setFinalizado(status.getFinalizado());
        return pedidoRepository.save(pedidoEncontrado);
    }

    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    public List<Pedido> listarPorPrestador(Long prestadorId) {
        return pedidoRepository.findAllByPrestadorId(prestadorId);
    }

    public Double somarValorFaturado(LocalDate dataInicio, LocalDate dataFim, Long prestadorId) {

        verificarPrestadorAdmin(prestadorId);

        LocalDateTime inicio = dataInicio.atTime(0, 0);
        LocalDateTime fim = dataFim.atTime(23, 59);

        Double faturamentoBruto = pedidoRepository.somarFaturamentoBruto(inicio, fim);

        return Objects.requireNonNullElse(faturamentoBruto, 0.0);

    }

    public Double calcularTicketMedio(LocalDate dataInicio, LocalDate dataFim, Long prestadorId) {

        verificarPrestadorAdmin(prestadorId);

        LocalDateTime inicio = dataInicio.atTime(0, 0);
        LocalDateTime fim = dataFim.atTime(23, 59);

        return pedidoRepository.somarFaturamentoBruto(inicio, fim) / pedidoRepository.contarPedidosPorPeriodo(inicio, fim);
    }

    public Double somarValorFaturadoPorPrestador(LocalDate dataInicio, LocalDate dataFim, Long prestadorId, Long prestadorBuscadoId) {

        verificarPrestadorAdmin(prestadorId);

        LocalDateTime inicio = dataInicio.atTime(0, 0);
        LocalDateTime fim = dataFim.atTime(23, 59);

        Double faturamentoBruto = pedidoRepository.somarFaturamentoBrutoPorPrestador(inicio, fim, prestadorBuscadoId);

        return Objects.requireNonNullElse(faturamentoBruto, 0.0);

    }

    private void verificarPrestadorAdmin (Long prestadorId) {
        Prestador prestador = prestadorService.buscarPorId(prestadorId);

        if (!(prestador.getCargo().getCargo().equalsIgnoreCase("ADMIN"))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

}
