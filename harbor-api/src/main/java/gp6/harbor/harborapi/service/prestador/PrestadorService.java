package gp6.harbor.harborapi.service.prestador;

import gp6.harbor.harborapi.api.enums.StatusPedidoEnum;
import gp6.harbor.harborapi.domain.cliente.Cliente;
import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.pedido.HorarioOcupadoDTO;
import gp6.harbor.harborapi.domain.pedido.Pedido;
import gp6.harbor.harborapi.domain.pedido.PedidoPrestador;
import gp6.harbor.harborapi.domain.pedido.PedidoV2;
import gp6.harbor.harborapi.domain.pedido.repository.HorarioOcupado;
import gp6.harbor.harborapi.domain.pedido.repository.PedidoPrestadorRepository;
import gp6.harbor.harborapi.domain.pedido.repository.PedidoRepository;
import gp6.harbor.harborapi.domain.prestador.AvaliacaoPrestador;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import gp6.harbor.harborapi.domain.prestador.repository.AvaliacaoRepository;
import gp6.harbor.harborapi.domain.prestador.repository.PrestadorRepository;
import gp6.harbor.harborapi.dto.prestador.dto.*;
import gp6.harbor.harborapi.exception.ConflitoException;
import gp6.harbor.harborapi.exception.NaoEncontradoException;
import gp6.harbor.harborapi.service.pedido.PedidoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class PrestadorService {

    private final PrestadorRepository prestadorRepository;
    private final PedidoRepository pedidoRepository;
    private final PrestadorMapperStruct prestadorMapperStruct;
    private final AvaliacaoRepository avaliacaoRepository;
    private final HorarioOcupadoMapper horarioOcupadoMapper;
    private final PedidoPrestadorRepository pedidoPrestadorRepository;


    //buscarPedidosAtendidosPorPrestador(prestador)
    public List<PedidoPrestador> buscarPedidosAtendidosPorPrestador(Long idPrestador) {

        return pedidoPrestadorRepository.findAllByPrestadorId(idPrestador);
    }

    public Prestador criar(Prestador prestador) {
        if (prestadorRepository.existsById(prestador.getId())) {
            throw new ConflitoException("Prestador Id");
        }
        return prestadorRepository.save(prestador);
    }

    public Prestador criarFuncionario(Prestador prestador) {
        if (prestadorRepository.existsById(prestador.getId())) {
            throw new ConflitoException("Prestador Id");
        }
        return prestadorRepository.save(prestador);
    }


    public List<PrestadorFuncionarioCriacao> listar() {
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        //se não achar o prestadorLogado crie uma excetion com uma mensagem de que precisa fazer o login
        Prestador prestadorLogado = prestadorRepository.findByEmail(emailUsuario).orElse(null);
        if (prestadorLogado == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "O usuário precisa estar logado");
        }

        Empresa empresa = prestadorLogado.getEmpresa();

        List<Prestador> prestadores = prestadorRepository.findByEmpresa(empresa);

        List<PrestadorFuncionarioCriacao> prestadoresDto = new ArrayList<>();

        //converta todos os prestadores da lista em PrestadorListagemDto
        for (Prestador prestador : prestadores) {
            PrestadorFuncionarioCriacao prestadorListagemDto = prestadorMapperStruct.toDto(prestador);
            prestadoresDto.add(prestadorListagemDto);
        }

        return prestadoresDto;
    }

    //listar funcionarios
    public List<FuncionarioListagemDto> listarFuncionarios() {
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();

        Prestador prestadorLogado = prestadorRepository.findByEmail(emailUsuario).orElse(null);
        if (prestadorLogado == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "O usuário precisa estar logado");
        }

        Empresa empresa = prestadorLogado.getEmpresa();

        List<Prestador> prestadores = prestadorRepository.findByEmpresa(empresa);

        List<FuncionarioListagemDto> funcionarios = new ArrayList<>();

        for (Prestador prestador : prestadores) {
            FuncionarioListagemDto prestadorListagemDto = prestadorMapperStruct.toFuncionarioListagemDto(prestador);
            funcionarios.add(prestadorListagemDto);
        }

        return funcionarios;
    }

    public void inativarPrestador(Long id) {
        Prestador prestador = buscarPorId(id);
        prestador.setAtivo(false);
        prestadorRepository.save(prestador);
    }


    public Prestador buscarPorId(Long id) {
        return prestadorRepository.findById(id).orElseThrow(
                () -> new NaoEncontradoException("Prestador")
        );
    }

    public List<Prestador> buscarPeloCpf(String cpf) {
        return prestadorRepository.findByCpfLike(cpf);
    }

    public boolean existePorCpf(String cpf) {
        return prestadorRepository.existsByCpf(cpf);
    }

    public List<Prestador> buscarPeloNome(String nome) {
        return prestadorRepository.findByNomeContainsIgnoreCase(nome);
    }

    public List<Prestador> buscarPorEmpresa(Empresa empresa) {
        return prestadorRepository.findByEmpresa(empresa);
    }

    public boolean existePorId(Long id) {
        return prestadorRepository.existsById(id);
    }

    public List<Pedido> listarPedidosPorPrestadorId(Long prestadorId) {
        buscarPorId(prestadorId);
        return pedidoRepository.findByPrestadorId(prestadorId);
    }
    @Transactional
    public void setFoto(Long id, String novaFoto) {
        prestadorRepository.setFoto(id, novaFoto);
    }

    public String getFoto(Long id) {
        return prestadorRepository.getFoto(id);
    }

    //listar horarios ocupados por prestadorid
    public List<HorarioOcupadoDTO> listarHorariosOcupadosPrestador(Long prestadorId){
        Prestador prestador = buscarPorId(prestadorId);

        List<HorarioOcupado> horariosOcupados = prestador.getHorariosOcupados();

        //converter horariosOcupados em horariosOcupadosDTO
        List<HorarioOcupadoDTO> horariosOcupadosDTO = new ArrayList<>();

        for (HorarioOcupado horarioOcupado : horariosOcupados) {
            HorarioOcupadoDTO horarioOcupadoDTO = horarioOcupadoMapper.toDto(horarioOcupado);
            horariosOcupadosDTO.add(horarioOcupadoDTO);
        }
        return horariosOcupadosDTO;
    }


    public List<LocalDateTime> listarHorariosOcupados(Long prestadorId) {
        buscarPorId(prestadorId);

        List<Pedido> pedidos = listarPedidosPorPrestadorId(prestadorId);

        List<LocalDateTime> horarios = new ArrayList<>();

        pedidos.forEach(pedido -> {
            AtomicReference<Integer> tempo = new AtomicReference<>(0);
            pedido.getPedidoServicos().forEach(pedidoServico -> tempo.updateAndGet(v -> v + pedidoServico.getServico().getTempoMedioEmMinutos()));
            LocalDateTime horarioFim = pedido.getDataAgendamento().plusMinutes(tempo.get());
            LocalDateTime horarioIteracao = pedido.getDataAgendamento();
            while(horarioIteracao.isBefore(horarioFim) || horarioIteracao.isEqual(horarioFim)) {
                if (horarioIteracao.plusMinutes(30).isEqual(horarioFim) || horarioIteracao.plusMinutes(30).isAfter(horarioFim)) {
                    horarios.add(horarioIteracao);
                    break;
                }
                horarios.add(horarioIteracao);
                horarioIteracao = horarioIteracao.plusMinutes(30);
            }
        });

        return horarios;
    }

    public AvaliacaoPrestador criarAvaliacaoPrestador(PedidoV2 pedido, Prestador prestador, Double estrelas, String comentario, Cliente cliente) {
        if (pedido.getCliente().getId() != cliente.getId() || pedido.getStatusPedidoEnum() != StatusPedidoEnum.FINALIZADO || pedido.getPedidoPrestador().stream().noneMatch(p -> p.getPrestador().getId().equals(prestador.getId()))) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Pedido não finalizado ou não pertence ao cliente ou prestador");
        }
        AvaliacaoPrestador avaliacaoPrestador = new AvaliacaoPrestador();
        avaliacaoPrestador.setPrestador(prestador);
        avaliacaoPrestador.setCliente(cliente);
        avaliacaoPrestador.setCodigoPedido(pedido.getCodigoPedido());
        avaliacaoPrestador.setEstrelas(estrelas);
        avaliacaoPrestador.setComentario(comentario);
        avaliacaoPrestador.setCnpjEmpresa(prestador.getEmpresa().getCnpj());
        return avaliacaoRepository.save(avaliacaoPrestador);
    }

    public Prestador ObterPrestadorLogado(String emailUsuario) {
        Prestador prestador = prestadorRepository.findByEmail(emailUsuario).orElse(null);
        if (prestador == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Você Precisa estar logado.");
        }
        return prestador;
    }


}
