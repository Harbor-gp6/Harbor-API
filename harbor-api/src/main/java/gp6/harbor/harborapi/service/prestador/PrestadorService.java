package gp6.harbor.harborapi.service.prestador;

import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.pedido.Pedido;
import gp6.harbor.harborapi.domain.pedido.repository.PedidoRepository;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import gp6.harbor.harborapi.domain.prestador.repository.PrestadorRepository;
import gp6.harbor.harborapi.exception.ConflitoException;
import gp6.harbor.harborapi.exception.NaoEncontradoException;
import gp6.harbor.harborapi.service.pedido.PedidoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class PrestadorService {

    private final PrestadorRepository prestadorRepository;
    private final PedidoRepository pedidoRepository;

    public Prestador criar(Prestador prestador) {
        if (prestadorRepository.existsById(prestador.getId())) {
            throw new ConflitoException("Prestador Id");
        }
        return prestadorRepository.save(prestador);
    }
    
    public List<Prestador> listar() {
        return prestadorRepository.findAll();
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
