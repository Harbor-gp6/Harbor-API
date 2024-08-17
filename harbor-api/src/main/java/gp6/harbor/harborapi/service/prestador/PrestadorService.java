package gp6.harbor.harborapi.service.prestador;

import gp6.harbor.harborapi.domain.cliente.Cliente;
import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.pedido.Pedido;
import gp6.harbor.harborapi.domain.pedido.repository.PedidoRepository;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import gp6.harbor.harborapi.domain.prestador.repository.PrestadorRepository;
import gp6.harbor.harborapi.dto.prestador.dto.PrestadorFuncionarioCriacao;
import gp6.harbor.harborapi.dto.prestador.dto.PrestadorListagemDto;
import gp6.harbor.harborapi.dto.prestador.dto.PrestadorMapperStruct;
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
    public void setFoto(Long id, byte[] novaFoto) {
        prestadorRepository.setFoto(id, novaFoto);
    }

    public byte[] getFoto(Long id) {
        return prestadorRepository.getFoto(id);
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

}
