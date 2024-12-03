package gp6.harbor.harborapi.service.pedido;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import gp6.harbor.harborapi.api.enums.FormaPagamentoEnum;
import gp6.harbor.harborapi.api.enums.StatusPedidoEnum;
import gp6.harbor.harborapi.domain.AtividadePedido.AtividadePedido;
import gp6.harbor.harborapi.domain.cliente.Cliente;
import gp6.harbor.harborapi.domain.cliente.repository.ClienteRepository;
import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.empresa.repository.EmpresaRepository;
import gp6.harbor.harborapi.domain.pedido.*;
import gp6.harbor.harborapi.domain.pedido.repository.HorarioOcupado;
import gp6.harbor.harborapi.domain.pedido.repository.PedidoV2Repository;
import gp6.harbor.harborapi.domain.prestador.repository.PrestadorRepository;
import gp6.harbor.harborapi.dto.pedido.dto.*;
import gp6.harbor.harborapi.service.AtividadePedido.AtividadePedidoService;
import gp6.harbor.harborapi.service.cliente.ClienteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final PedidoV2Mapper pedidoV2Mapper;
    private final PedidoV2MapperV2 pedidoV2MapperV2;
    private final AtividadePedidoService atividadePedidoService;
    private static final Logger log = LoggerFactory.getLogger(PedidoService.class);


    @Transactional
    public PedidoV2 criarPedidoV2(PedidoV2CriacaoDto pedidoDto) {
        PedidoV2 pedido = pedidoV2Mapper.toEntity(pedidoDto);
        UUID codigoPedido = UUID.randomUUID();

        pedido.setCodigoPedido(codigoPedido);
        List<PedidoPrestador> pedidoPrestadores = pedidoV2Mapper.toPedidoPrestadorEntityList(pedidoDto.getPedidoPrestador());
        List<PedidoProdutoV2> pedidoProdutos = pedidoV2Mapper.toPedidoProdutoV2EntityList(pedidoDto.getPedidoProdutos());

        pedido.setPedidoPrestador(pedidoPrestadores);
        pedido.setPedidoProdutos(pedidoProdutos);

        if (pedido.getEmpresa() == null && pedidoDto.getCnpjEmpresa() != null) {
            Empresa empresa = empresaRepository.findByCnpj(pedidoDto.getCnpjEmpresa()).orElse(null);
            if (empresa == null) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Empresa não encontrada.");
            }
            pedido.setEmpresa(empresa);
        }

        if (pedido.getCliente() != null && pedido.getCliente().getCpf() != null) {
            Cliente cliente = clienteRepository.findByCpf(pedido.getCliente().getCpf()).orElse(null);
            if (cliente == null && clienteService.validarCliente(pedido.getCliente())) {
                pedido.getCliente().setEmpresa(pedido.getEmpresa());
                cliente = clienteRepository.save(pedido.getCliente());
            }
            pedido.setCliente(cliente);
        }

        // Atribui serviços aos PedidoPrestador
        LocalDateTime horarioAtual = pedido.getDataAgendamento();
        List<PedidoPrestador> pedidoPrestadorList = new ArrayList<>();
        for (PedidoPrestadorDto pedidoPrestador : pedidoDto.getPedidoPrestador()) {
            if (pedidoPrestador.getPrestadorId() == null || pedidoPrestador.getServicoId() == null) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Prestador ou Serviço não encontrado no PedidoPrestador.");
            }

            Servico servico = servicoService.buscaPorId(pedidoPrestador.getServicoId());
            if (servico == null || servico.getTempoMedioEmMinutos() == null) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "O serviço ou seu tempo médio está inválido.");
            }
            //procura o prestador pelo id
            Prestador prestador = prestadorService.buscarPorId(pedidoPrestador.getPrestadorId());
            if (prestador == null) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Prestador não encontrado.");
            }
            if (!prestador.isAtivo()) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Prestador inativo.");
            }

            //adiciona o serviço ao pedido
            PedidoPrestador pedidoPrestadorNovo = pedidoV2Mapper.toPedidoPrestadorEntity(pedidoPrestador);
            pedidoPrestadorNovo.setPrestador(prestador);
            pedidoPrestadorNovo.setServico(servico);
            pedidoPrestadorNovo.setPedido(pedido);
            pedidoPrestadorNovo.setDataInicio(horarioAtual);
            pedidoPrestadorNovo.calcularDataFim();

            // Define a data de início e fim do serviço para o prestador
            HorarioOcupado horarioOcupado = new HorarioOcupado();
            horarioOcupado.setDataInicio(horarioAtual);
            horarioOcupado.setDataFim(horarioAtual.plusMinutes(servico.getTempoMedioEmMinutos()));
            horarioOcupado.setPrestador(prestador);
            horarioOcupado.setCodigoPedido(codigoPedido);

            // Adiciona o horário ocupado na lista de horários do prestador
            if (!prestador.adicionarHorarioOcupado(horarioOcupado)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Horário conflita com outro agendamento.");
            }

            // Atualiza o horário atual para o próximo serviço
            horarioAtual = horarioOcupado.getDataFim();

            pedidoPrestadorList.add(pedidoPrestadorNovo);
        }
        pedido.setPedidoPrestador(pedidoPrestadorList);

        List<PedidoProdutoV2> pedidoProdutoList = new ArrayList<>();
        for (PedidoProdutoV2Dto pedidoProdutoDto : pedidoDto.getPedidoProdutos()) {
            Produto produto = produtoService.buscarPorId(pedidoProdutoDto.getId());
            if (produto == null) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Produto não encontrado.");
            }

            PedidoProdutoV2 pedidoProduto = new PedidoProdutoV2();
            pedidoProduto.setPedido(pedido);
            pedidoProduto.setProduto(produto);
            pedidoProduto.setQuantidade(pedidoProdutoDto.getQuantidade());

            pedidoProdutoList.add(pedidoProduto);

        }
        pedido.setPedidoProdutos(pedidoProdutoList);

        // Valida o pedido
       if (!validarPedidoV2(pedido)) {
           throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Pedido inválido.");
       }

       pedido.calcularTotalProduto();
       pedido.calcularTotalServico();
       pedido.calcularTotalPedido();


        // Salva o pedido e atualiza os horários ocupados do prestador
        PedidoV2 pedidoSalvo = pedidoV2Repository.save(pedido);
        for (PedidoPrestador pedidoPrestador : pedido.getPedidoPrestador()) {
            Prestador prestador = prestadorService.buscarPorId(pedidoPrestador.getPrestador().getId());
            prestadorRepository.save(prestador);  // Salva o prestador com a lista atualizada de horários ocupados
        }
        //dar detalhes do pedido no email
        String emailFormatado = emailService.formatarEmail(pedido.getCliente().getEmail(), "AGENDAMENTO REALIZADO COM SUCESSO", pedidoSalvo);
        try {
            emailService.sendEmail(pedido.getCliente().getEmail(), "Agendamento realizado", emailFormatado);
        } catch (Exception e) {
            // Registrar o erro sem lançar exceção para não interromper o fluxo
            log.error("Erro ao enviar e-mail para {}: {}", pedido.getCliente().getEmail(), e.getMessage());
        }


        AtividadePedido atividadePedido = atividadePedidoService.criarAtividadePedido(pedidoSalvo);

        return pedidoSalvo;
    }

    @Transactional
    public PedidoV2 atualizarPedidoV2(Integer idPedido, PedidoV2CriacaoDto pedidoDto) {
        PedidoV2 pedidoEncontrado = pedidoV2Repository.findById(idPedido)
                .orElseThrow(() -> new NaoEncontradoException("Pedido"));

        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Prestador prestadorLogado = prestadorRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Prestador não encontrado."));

        if (pedidoEncontrado.getStatusPedidoEnum() == StatusPedidoEnum.FINALIZADO) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Pedido já finalizado.");
        }
        if (pedidoEncontrado.getStatusPedidoEnum() == StatusPedidoEnum.CANCELADO) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Pedido cancelado.");
        }

        if (!pedidoEncontrado.getEmpresa().getId().equals(prestadorLogado.getEmpresa().getId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Pedido não pertence à empresa do prestador.");
        }

        pedidoEncontrado.setDataAgendamento(pedidoDto.getDataAgendamento());
        UUID codigoPedido = pedidoEncontrado.getCodigoPedido();
        if (pedidoDto.getCnpjEmpresa() != null) {
            Empresa empresa = empresaRepository.findByCnpj(pedidoDto.getCnpjEmpresa())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                            "Empresa não encontrada."));
            pedidoEncontrado.setEmpresa(empresa);
        }

        if (pedidoDto.getCliente() != null && pedidoDto.getCliente().getCpf() != null) {
            Cliente cliente = clienteRepository.findByCpf(pedidoDto.getCliente().getCpf())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                            "Cliente não encontrado."));
            cliente.setNome(pedidoDto.getCliente().getNome());
            cliente.setSobrenome(pedidoDto.getCliente().getSobrenome());
            cliente.setEmail(pedidoDto.getCliente().getEmail());
            cliente.setTelefone(pedidoDto.getCliente().getTelefone());

            pedidoEncontrado.setCliente(cliente);
        }

        LocalDateTime horarioAtual = pedidoEncontrado.getDataAgendamento();

        // Limpa a lista atual de PedidoPrestador antes de adicionar os novos
        pedidoEncontrado.getPedidoPrestador().clear();
        for (PedidoPrestadorDto pedidoPrestadorDto : pedidoDto.getPedidoPrestador()) {
            Prestador prestador = prestadorService.buscarPorId(pedidoPrestadorDto.getPrestadorId());
            Servico servico = servicoService.buscaPorId(pedidoPrestadorDto.getServicoId());

            PedidoPrestador pedidoPrestador = pedidoV2Mapper.toPedidoPrestadorEntity(pedidoPrestadorDto);
            pedidoPrestador.setPrestador(prestador);
            pedidoPrestador.setServico(servico);
            pedidoPrestador.setPedido(pedidoEncontrado);
            pedidoPrestador.setDataInicio(horarioAtual);
            pedidoPrestador.setDataFim(horarioAtual.plusMinutes(servico.getTempoMedioEmMinutos()));

            // Verifica se o horário já está ocupado pelo mesmo pedido
            HorarioOcupado horarioExistente = prestador.getHorariosOcupados().stream()
                    .filter(h -> h.getDataInicio().equals(pedidoPrestador.getDataInicio()) &&
                            h.getDataFim().equals(pedidoPrestador.getDataFim()) &&
                            h.getPrestador().equals(prestador))
                    .findFirst()
                    .orElse(null);

            // Remove os horários existentes que encontrou acima
            if (horarioExistente != null) {
                prestador.getHorariosOcupados().remove(horarioExistente);
            }

            if (horarioExistente != null) {
                // Atualiza o horário existente
                horarioExistente.setDataInicio(pedidoPrestador.getDataInicio());
                horarioExistente.setDataFim(pedidoPrestador.getDataFim());
            } else {
                // Valida e adiciona novo horário ocupado para o prestador
                HorarioOcupado novoHorarioOcupado = new HorarioOcupado();
                novoHorarioOcupado.setDataInicio(pedidoPrestador.getDataInicio());
                novoHorarioOcupado.setDataFim(pedidoPrestador.getDataFim());
                novoHorarioOcupado.setPrestador(prestador);
                novoHorarioOcupado.setCodigoPedido(codigoPedido);

                if (!prestador.adicionarHorarioOcupado(novoHorarioOcupado)) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Horário conflita com outro agendamento.");
                }
            }
            pedidoEncontrado.getPedidoPrestador().add(pedidoPrestador);
            horarioAtual = pedidoPrestador.getDataFim();
        }

        pedidoEncontrado.getPedidoProdutos().clear();
        for (PedidoProdutoV2Dto pedidoProdutoDto : pedidoDto.getPedidoProdutos()) {
            Produto produto = produtoService.buscarPorId(pedidoProdutoDto.getId());
            if (produto == null) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Produto não encontrado.");
            }

            PedidoProdutoV2 pedidoProduto = new PedidoProdutoV2();
            pedidoProduto.setPedido(pedidoEncontrado);
            pedidoProduto.setProduto(produto);
            pedidoProduto.setQuantidade(pedidoProdutoDto.getQuantidade());

            pedidoEncontrado.getPedidoProdutos().add(pedidoProduto);
        }

        if (!validarPedidoV2(pedidoEncontrado)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Pedido inválido.");
        }
        
        pedidoEncontrado.calcularTotalProduto();
        pedidoEncontrado.calcularTotalServico();
        pedidoEncontrado.calcularTotalPedido();

        return pedidoV2Repository.save(pedidoEncontrado);
    }

    public PedidoV2 finalizarPedidoV2(UUID codigoPedido) {
        PedidoV2 pedidoEncontrado = pedidoV2Repository.findByCodigoPedido(codigoPedido);
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Prestador prestador = prestadorRepository.findByEmail(emailUsuario).orElse(null);

        if (pedidoEncontrado.getEmpresa().getId() != prestador.getEmpresa().getId()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Pedido não pertence a empresa do prestador");
        }

        if (pedidoEncontrado.getStatusPedidoEnum() == StatusPedidoEnum.FINALIZADO) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Pedido já finalizado.");
        }
        if (pedidoEncontrado.getStatusPedidoEnum() == StatusPedidoEnum.CANCELADO) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Pedido cancelado.");
        }

        pedidoEncontrado.getPedidoPrestador().forEach(pedidoPrestador -> {
            pedidoPrestador.getPrestador().getHorariosOcupados().removeIf(
                    horarioOcupado -> horarioOcupado.getCodigoPedido().equals(pedidoEncontrado.getCodigoPedido()));
        });

        pedidoEncontrado.setStatusPedidoEnum(StatusPedidoEnum.FINALIZADO);

        String emailFormatado = emailService.formatarEmailPedidoFinalizado(pedidoEncontrado);
        emailService.sendEmail(pedidoEncontrado.getCliente().getEmail(), "Agendamento finalizado", emailFormatado);

        AtividadePedido atividadePedido = atividadePedidoService.criarAtividadePedido(pedidoEncontrado);


        return pedidoV2Repository.save(pedidoEncontrado);
    }

    public PedidoV2 cancelarPedidoV2(UUID codigoPedido) {
        PedidoV2 pedidoEncontrado = pedidoV2Repository.findByCodigoPedido(codigoPedido);
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Prestador prestador = prestadorRepository.findByEmail(emailUsuario).orElse(null);

        if (pedidoEncontrado.getEmpresa().getId() != prestador.getEmpresa().getId()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Pedido não pertence a empresa do prestador");
        }

        if (pedidoEncontrado.getStatusPedidoEnum() == StatusPedidoEnum.FINALIZADO) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Pedido já finalizado.");
        }
        if (pedidoEncontrado.getStatusPedidoEnum() == StatusPedidoEnum.CANCELADO) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Pedido cancelado.");
        }

        pedidoEncontrado.getPedidoPrestador().forEach(pedidoPrestador -> {
            pedidoPrestador.getPrestador().getHorariosOcupados().removeIf(
                    horarioOcupado -> horarioOcupado.getCodigoPedido().equals(pedidoEncontrado.getCodigoPedido()));
        });

        pedidoEncontrado.setStatusPedidoEnum(StatusPedidoEnum.CANCELADO);

        String emailFormatado = "Pedido cancelado :(";

        emailService.sendEmail(pedidoEncontrado.getCliente().getEmail(), "Pedido Cancelado", emailFormatado);

        AtividadePedido atividadePedido = atividadePedidoService.criarAtividadePedido(pedidoEncontrado);

        return pedidoV2Repository.save(pedidoEncontrado);
    }


    public Pedido criarPedido(Pedido novoPedido, List<Integer> servicosIds) {
        if (novoPedido.getDataAgendamento().getHour() < novoPedido.getPrestador().getEmpresa().getHorarioAbertura()
                .getHour()
                || novoPedido.getDataAgendamento().getHour() > novoPedido.getPrestador().getEmpresa()
                        .getHorarioFechamento().getHour()) {
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
        String message = "Voce tem um agendamento em " + pedidoSalvo.getPrestador().getEmpresa().getNomeFantasia()
                + " para dia " + pedidoSalvo.getDataAgendamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                + " às " + pedidoSalvo.getDataAgendamento().format(DateTimeFormatter.ofPattern("HH:mm")) + " com "
                + pedidoSalvo.getPrestador().getNome() + " " + pedidoSalvo.getPrestador().getSobrenome();

        emailService.sendEmail(pedidoSalvo.getCliente().getEmail(), subject, message);

        subject = "VOCÊ TEM UM NOVO SERVIÇO AGENDADO";
        message = "Voce tem um serviço agendado para dia "
                + pedidoSalvo.getDataAgendamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " às "
                + pedidoSalvo.getDataAgendamento().format(DateTimeFormatter.ofPattern("HH:mm")) + " com cliente "
                + pedidoSalvo.getCliente().getNome() + " " + pedidoSalvo.getCliente().getSobrenome();

        emailService.sendEmail(pedidoSalvo.getPrestador().getEmail(), subject, message);

        PedidoController.filaPedido.adicionarPedido(pedidoSalvo);

        return pedidoSalvo;
    }

    public Pedido adicionarProduto(Integer pedidoId, PedidoAtualizacaoProdutoDto produtos) {
        Pedido pedidoEncontrado = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new NaoEncontradoException("Pedido"));

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
        Pedido pedidoEncontrado = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new NaoEncontradoException("Pedido"));

        pedidoEncontrado.setFinalizado(status.getFinalizado());
        return pedidoRepository.save(pedidoEncontrado);
    }

    public List<PedidoV2DTO> listarPedidosV2() {
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Prestador prestador = prestadorRepository.findByEmail(emailUsuario).orElseThrow(() -> new RuntimeException("Prestador não encontrado"));
        Empresa empresa = prestador.getEmpresa();

        List<PedidoV2> pedidos = pedidoV2Repository.findByEmpresa(empresa);
        // Mapeando para DTO antes de retornar
        return pedidoV2MapperV2.toDtoList(pedidos);
    }

    public List<PedidoV2DTO> listarPedidosV2Abertos() {
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Prestador prestador = prestadorRepository.findByEmail(emailUsuario).orElse(null);
        Empresa empresa = prestador.getEmpresa();
        if (emailUsuario == null || prestador == null || empresa == null || !prestador.isAtivo()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Você Precisa estar logado.");
        }

        List<PedidoV2> pedidos = pedidoV2Repository.findByEmpresaAndStatusPedidoEnum(prestador.getEmpresa(),
                StatusPedidoEnum.ABERTO);

        return pedidoV2MapperV2.toDtoList(pedidos);
    }

    public List<PedidoV2DTO> listarPedidosV2AbertosPorPrestador() {
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Prestador prestador = prestadorRepository.findByEmail(emailUsuario).orElse(null);
        Empresa empresa = prestador.getEmpresa();
        if (emailUsuario == null || prestador == null || empresa == null || !prestador.isAtivo()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Você Precisa estar logado.");
        }

        List<PedidoV2> pedidos = pedidoV2Repository.findByPedidoPrestadorPrestadorCpfAndStatusPedidoEnum(prestador.getCpf(), StatusPedidoEnum.ABERTO);

        return pedidoV2MapperV2.toDtoList(pedidos);
    }

    //listarPedidosV2PorData
    public List<PedidoV2DTO> listarPedidosV2PorData(LocalDate data) {
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Prestador prestador = prestadorRepository.findByEmail(emailUsuario).orElse(null);
        Empresa empresa = prestador.getEmpresa();
        if (emailUsuario == null || prestador == null || empresa == null || !prestador.isAtivo()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Você Precisa estar logado.");
        }

        LocalDateTime dataInicio = data.atStartOfDay();
        LocalDateTime dataFim = data.atTime(23, 59, 59);

        List<PedidoV2> pedidos = pedidoV2Repository.findByDataAgendamentoBetweenAndPedidoPrestadorPrestadorCpf(dataInicio, dataFim, prestador.getCpf());


        return pedidoV2MapperV2.toDtoList(pedidos);
    }

    public List<PedidoV2DTO> listarPedidosV2Finalizados() {
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Prestador prestador = prestadorRepository.findByEmail(emailUsuario).orElse(null);
        Empresa empresa = prestador.getEmpresa();
        if (emailUsuario == null || prestador == null || empresa == null || !prestador.isAtivo()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Você Precisa estar logado.");
        }

        List<PedidoV2> pedidos = pedidoV2Repository.findByEmpresaAndStatusPedidoEnum(prestador.getEmpresa(),
                StatusPedidoEnum.FINALIZADO);

        return pedidoV2MapperV2.toDtoList(pedidos);
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

        return pedidoRepository.somarFaturamentoBruto(inicio, fim)
                / pedidoRepository.contarPedidosPorPeriodo(inicio, fim);
    }

    // somarValorFaturadoPorPrestadorPorPeriodo
    public Double somarValorFaturadoPorPrestadorPorPeriodo(LocalDate dataInicio, LocalDate dataFim, Long prestadorId) {

        verificarPrestadorAdmin(prestadorId);

        LocalDateTime inicio = dataInicio.atTime(0, 0);
        LocalDateTime fim = dataFim.atTime(23, 59);

        Double faturamentoBruto = pedidoRepository.somarFaturamentoBrutoPorPrestador(inicio, fim, prestadorId);

        return Objects.requireNonNullElse(faturamentoBruto, 0.0);

    }

    public Double somarValorFaturadoPorPrestador(LocalDate dataInicio, LocalDate dataFim, Long prestadorId,
            Long prestadorBuscadoId) {

        verificarPrestadorAdmin(prestadorId);

        LocalDateTime inicio = dataInicio.atTime(0, 0);
        LocalDateTime fim = dataFim.atTime(23, 59);

        Double faturamentoBruto = pedidoRepository.somarFaturamentoBrutoPorPrestador(inicio, fim, prestadorBuscadoId);

        return Objects.requireNonNullElse(faturamentoBruto, 0.0);

    }

    public Double somarValorFaturado(LocalDate dataInicio, LocalDate dataFim) {
        // pegar email do prestador logado
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Prestador prestador = prestadorService.ObterPrestadorLogado(emailUsuario);

        verificarPrestadorAdmin(prestador.getId());

        LocalDateTime inicio = dataInicio.atTime(0, 0);
        LocalDateTime fim = dataFim.atTime(23, 59);

        Double faturamentoBruto = pedidoV2Repository.somarFaturamentoBrutoPorEmpresa(inicio, fim,
                prestador.getEmpresa().getId());

        return Objects.requireNonNullElse(faturamentoBruto, 0.0);

    }

    // qtsdePedidosV2PorFormaPagamento
    public List<Object[]> PedidosPorFormaPagamento(LocalDate dataInicio, LocalDate dataFim) {
        Prestador usuarioLogado = prestadorService
                .ObterPrestadorLogado(SecurityContextHolder.getContext().getAuthentication().getName());

        verificarPrestadorAdmin(usuarioLogado.getId());

        LocalDateTime inicio = dataInicio.atTime(0, 0);
        LocalDateTime fim = dataFim.atTime(23, 59);

        return pedidoV2Repository.countByDataAgendamentoBetweenAndEmpresaIdAndFormaPagamentoEnum(inicio, fim,
                usuarioLogado.getEmpresa().getId());
    }

    private void verificarPrestadorAdmin(Long prestadorId) {
        Prestador prestador = prestadorService.buscarPorId(prestadorId);

        if (!(prestador.getCargo().getCargo().equalsIgnoreCase("ADMIN"))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    public boolean validarPedidoV2(PedidoV2 pedidoV2) {
        if (!clienteService.validarCliente(pedidoV2.getCliente())) {
            return false;
        }
        if (pedidoV2.getFormaPagamentoEnum() == null) {
            return false;
        }
        if (pedidoV2.getDataAgendamento() == null) {
            return false;
        }
        if (pedidoV2.getPedidoPrestador().isEmpty()) {
            return false;
        }

        return true;
    }

    public boolean pedidoFinalizado(Integer idPedido) {
        PedidoV2 pedido = pedidoV2Repository.findById(idPedido).orElseThrow(() -> new NaoEncontradoException("Pedido"));
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Prestador prestador = prestadorRepository.findByEmail(emailUsuario).orElse(null);
        if (pedido.getEmpresa().getId() != prestador.getEmpresa().getId()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Pedido não pertence a empresa do prestador");
        }
        return pedido.getStatusPedidoEnum() == StatusPedidoEnum.FINALIZADO;
    }

    public PedidoV2DTO buscarPorCodigoPedido(UUID codigoPedido) {
        PedidoV2 pedidos = pedidoV2Repository.findByCodigoPedido(codigoPedido);
        return pedidoV2MapperV2.toDto(pedidos);
    }

}
