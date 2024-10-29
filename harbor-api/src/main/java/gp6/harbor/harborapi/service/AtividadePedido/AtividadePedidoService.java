package gp6.harbor.harborapi.service.AtividadePedido;

import gp6.harbor.harborapi.domain.AtividadePedido.AtividadePedido;
import gp6.harbor.harborapi.domain.AtividadePedido.repository.AtividadePedidoRepository;
import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.pedido.PedidoPrestador;
import gp6.harbor.harborapi.domain.pedido.PedidoV2;
import gp6.harbor.harborapi.domain.pedido.repository.PedidoV2Repository;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import gp6.harbor.harborapi.domain.prestador.repository.PrestadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AtividadePedidoService {
    private final AtividadePedidoRepository atividadePedidoRepository;
    private final PrestadorRepository prestadorRepository;
    private final PedidoV2Repository pedidoV2Repository;
    public AtividadePedido criarAtividadePedido(PedidoV2 pedido) {
        List<String> cpfs = new ArrayList<>();
        for (PedidoPrestador pedidoPrestador : pedido.getPedidoPrestador()) {
            if (!cpfs.contains(pedidoPrestador.getPrestador().getCpf())){
                cpfs.add(pedidoPrestador.getPrestador().getCpf());
            }
        }

        AtividadePedido atividadePedido = new AtividadePedido();
        atividadePedido.setCodigoPedido(pedido.getCodigoPedido());
        atividadePedido.setStatusPedidoEnum(pedido.getStatusPedidoEnum());
        atividadePedido.setCnpjEmpresa(pedido.getEmpresa().getCnpj());
        atividadePedido.setCpfs(cpfs);
        atividadePedido.setNomeCliente(pedido.getCliente().getNome() + " " + pedido.getCliente().getSobrenome());
        atividadePedido.setServico(pedido.getPedidoPrestador().get(0).getServico().getDescricaoServico());

        return atividadePedidoRepository.save(atividadePedido);
    }

    public List<AtividadePedido> listarAtividadesPedido() {
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Prestador prestadorLogado = prestadorRepository.findByEmail(emailUsuario).orElse(null);
        if (prestadorLogado == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "O usuário precisa estar logado");
        }
        Empresa empresa = prestadorLogado.getEmpresa();

        Prestador prestador = prestadorRepository.findByCpf(prestadorLogado.getCpf());

        if (!prestador.getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName()) && !"ADMIN".equals(prestador.getCargo())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário não tem permissão para atualizar funcionários");
        }

        List<AtividadePedido> pedidos = atividadePedidoRepository.findByCnpjEmpresa(empresa.getCnpj());
        return pedidos;
    }

    public AtividadePedido buscarAtividadePedido(UUID codigoPedido) {
        AtividadePedido pedido = atividadePedidoRepository.findByCodigoPedido(codigoPedido);
        return pedido;
    }

    public List<AtividadePedido> atividadePedidoPorUsuarioLogado() {
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Prestador prestadorLogado = prestadorRepository.findByEmail(emailUsuario).orElse(null);
        if (prestadorLogado == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "O usuário precisa estar logado");
        }
        Empresa empresa = prestadorLogado.getEmpresa();

        Prestador prestador = prestadorRepository.findByCpf(prestadorLogado.getCpf());

        if (!prestador.getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName()) && !"ADMIN".equals(prestador.getCargo())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário não tem permissão para atualizar funcionários");
        }
        List<AtividadePedido> atividadePedido = atividadePedidoRepository.findByCpfsContaining(prestador.getCpf());

        if (atividadePedido != null) {
            List<AtividadePedido> pedidos = atividadePedidoRepository.findByCnpjEmpresa(empresa.getCnpj());
        }
        return atividadePedido;
    }
}
