package gp6.harbor.harborapi.service.pedido;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import gp6.harbor.harborapi.domain.cliente.Cliente;
import gp6.harbor.harborapi.domain.cliente.repository.ClienteRepository;
import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.empresa.repository.EmpresaRepository;
import gp6.harbor.harborapi.domain.pedido.*;
import gp6.harbor.harborapi.domain.pedido.repository.HorarioOcupado;
import gp6.harbor.harborapi.domain.pedido.repository.PedidoV2Repository;
import gp6.harbor.harborapi.domain.prestador.repository.PrestadorRepository;
import gp6.harbor.harborapi.service.cliente.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import gp6.harbor.harborapi.api.controller.pedido.PedidoController;
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

    private final PrestadorRepository prestadorRepository;
    private final PedidoV2Repository pedidoV2Repository;
    private final ClienteRepository clienteRepository;
    private final PedidoRepository pedidoRepository;
    private final EmpresaRepository empresaRepository;
    private final PedidoServicoService pedidoServicoService;
    private final PedidoProdutoService pedidoProdutoService;
    private final PrestadorService prestadorService;
    private final ProdutoService produtoService;
    private final ServicoService servicoService;
    private final EmailService emailService;
    private final ClienteService clienteService;


    @Transactional
    public PedidoV2 criarPedidoV2(PedidoV2 pedido) {
        // Verifica e salva cliente
        if (pedido.getCliente().getCpf() != null) {
            Cliente cliente = clienteRepository.findByCpf(pedido.getCliente().getCpf()).orElse(null);
            if (cliente == null) {
                cliente = clienteRepository.save(pedido.getCliente());
            }
            if (clienteService.validarCliente(cliente)) {
                pedido.setCliente(cliente);
            }
        }

        // Verifica e salva empresa
        String cnpjEmpresa = pedido.getEmpresa().getCnpj();
        if (cnpjEmpresa != null) {
            Empresa empresa = empresaRepository.findByCnpj(cnpjEmpresa).orElse(null);
            if (empresa == null) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
            }
            pedido.setEmpresa(empresa);
        }

        // Busca e atribui serviços ao PedidoPrestador
        for (PedidoPrestador pedidoPrestador : pedido.getPedidoPrestador()) {
            Servico servico = servicoService.buscaPorId(pedidoPrestador.getServico().getId());
            if (servico == null || servico.getTempoMedioEmMinutos() == null) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "O serviço ou seu tempo médio está inválido.");
            }
            pedidoPrestador.setServico(servico);
        }

        // Hora inicial do primeiro serviço
        LocalDateTime horarioAtual = pedido.getDataAgendamento();  // Supondo que pedido tenha um campo dataAgendamento

        // Associa cada PedidoPrestador ao Pedido e calcula os horários
        for (PedidoPrestador pedidoPrestador : pedido.getPedidoPrestador()) {
            pedidoPrestador.setPedido(pedido);

            // Define a data de início e fim do serviço para o prestador
            HorarioOcupado horarioOcupado = new HorarioOcupado();
            horarioOcupado.setDataInicio(horarioAtual);
            horarioOcupado.setDataFim(horarioAtual.plusMinutes(pedidoPrestador.getServico().getTempoMedioEmMinutos()));

            // Associa o prestador ao HorarioOcupado
            Prestador prestador = prestadorService.buscarPorId(pedidoPrestador.getPrestador().getId());
            horarioOcupado.setPrestador(prestador);

            // Adiciona o horário ocupado na lista de horários do prestador
            if (!prestador.adicionarHorarioOcupado(horarioOcupado)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Horário conflita com outro agendamento.");
            }

            // Atualiza o horário atual para o próximo serviço
            horarioAtual = horarioOcupado.getDataFim();
        }

        // Valida o pedido
        if (!validarPedidoV2(pedido)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        // Salva o pedido e os horários ocupados do prestador
        PedidoV2 pedidoSalvo = pedidoV2Repository.save(pedido);
        for (PedidoPrestador pedidoPrestador : pedido.getPedidoPrestador()) {
            Prestador prestador = prestadorService.buscarPorId(pedidoPrestador.getPrestador().getId());
            prestadorRepository.save(prestador);  // Salva o prestador com a lista atualizada de horários ocupados
        }

        return pedidoSalvo;
    }


    public PedidoV2 finalizarPedidoV2(Integer pedidoId) {
        PedidoV2 pedidoEncontrado = pedidoV2Repository.findById(pedidoId).orElseThrow(() -> new NaoEncontradoException("Pedido"));

        //verificar se o pedido pertence a empresa que o prestador está vinculado para poder finalizar
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Prestador prestador = prestadorRepository.findByEmail(emailUsuario).orElse(null);
        pedidoEncontrado.setFinalizado(true);
        return pedidoV2Repository.save(pedidoEncontrado);
    }
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

         String subject = "AGENDAMENTO REALIZADO COM SUCESSO";
         String message = "Voce tem um agendamento em " + pedidoSalvo.getPrestador().getEmpresa().getNomeFantasia() + " para dia " + pedidoSalvo.getDataAgendamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " às " + pedidoSalvo.getDataAgendamento().format(DateTimeFormatter.ofPattern("HH:mm")) + " com " + pedidoSalvo.getPrestador().getNome() + " " + pedidoSalvo.getPrestador().getSobrenome();

         emailService.sendEmail(pedidoSalvo.getCliente().getEmail(), subject, message);

         subject = "VOCÊ TEM UM NOVO SERVIÇO AGENDADO";
         message = "Voce tem um serviço agendado para dia " + pedidoSalvo.getDataAgendamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " às " + pedidoSalvo.getDataAgendamento().format(DateTimeFormatter.ofPattern("HH:mm")) + " com cliente " + pedidoSalvo.getCliente().getNome() + " " + pedidoSalvo.getCliente().getSobrenome();

         emailService.sendEmail(pedidoSalvo.getPrestador().getEmail(), subject, message);

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

    public List<PedidoV2> listarPedidosV2() {
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Prestador prestador = prestadorRepository.findByEmail(emailUsuario).orElse(null);
        Empresa empresa = prestador.getEmpresa();
        return pedidoV2Repository.findByEmpresa(empresa);
    }

    //listar por cpf
    public List<PedidoV2> listarPorCpf(String cpf) {
        return pedidoV2Repository.findByPedidoPrestadorPrestadorCpf(cpf);
    }

    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    public List<Pedido> listarPorPrestador(Long prestadorId) {
        return pedidoRepository.findAllByPrestadorId(prestadorId);
    }

    public void deletar (Integer prestadorId) {
        pedidoRepository.deleteById(prestadorId);
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

    //validar pedidoV2
    public boolean validarPedidoV2(PedidoV2 pedidoV2){
        //validar se todos os atributos do pedidoV2 são validos
        if (!clienteService.validarCliente(pedidoV2.getCliente())) {
            return false;
        }

        return true;
    }

}
