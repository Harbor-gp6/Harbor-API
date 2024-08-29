package gp6.harbor.harborapi.service.prestador;

import gp6.harbor.harborapi.api.enums.StatusPedidoEnum;
import gp6.harbor.harborapi.domain.cliente.Cliente;
import gp6.harbor.harborapi.domain.cliente.repository.ClienteRepository;
import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.pedido.PedidoV2;
import gp6.harbor.harborapi.domain.pedido.repository.PedidoRepository;
import gp6.harbor.harborapi.domain.pedido.repository.PedidoV2Repository;
import gp6.harbor.harborapi.domain.prestador.AvaliacaoPrestador;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import gp6.harbor.harborapi.domain.prestador.repository.AvaliacaoRepository;
import gp6.harbor.harborapi.domain.prestador.repository.PrestadorRepository;
import gp6.harbor.harborapi.dto.prestador.dto.AvaliacaoPrestadorDto;
import gp6.harbor.harborapi.dto.prestador.dto.PrestadorEstrelasDto;
import gp6.harbor.harborapi.service.cliente.ClienteService;
import gp6.harbor.harborapi.service.pedido.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AvaliacaoPrestadorService {
    private final AvaliacaoRepository avaliacaoRepository;
    private final PrestadorService prestadorService;
    private final PedidoService pedidoService;
    private final PedidoV2Repository pedidoV2Repository;
    private final ClienteService clienteService;
    private final PrestadorRepository prestadorRepository;

    //criar avaliação de prestador caso o pedido já esteja finalizado
    public void criarAvaliacaoPrestador(AvaliacaoPrestadorDto avaliacaoPrestadorDto) {
        Cliente cliente = clienteService.buscarPorId(avaliacaoPrestadorDto.getIdCliente());
        PedidoV2 pedido = pedidoV2Repository.findByCodigoPedido(avaliacaoPrestadorDto.getCodigoPedido()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
        List<Prestador> prestadoresAvaliados = new ArrayList<>();
        //preciso percorrer a lista de prestadores do pedido e verificar se o prestador o prestador existe, se ele fez o serviço e se o pedido está finalizado e ai sim criar a avaliação
        //faça um for
        for (PrestadorEstrelasDto prestadorEstrelasDto : avaliacaoPrestadorDto.getAvaliacoes()){
            Prestador prestador = prestadorService.buscarPorId(prestadorEstrelasDto.getIdPrestador());
            if (pedido.getPedidoPrestador().stream().noneMatch(p -> p.getPrestador().getId().equals(prestador.getId()))){
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Prestador não encontrado no pedido");
            }
            if (pedido.getStatusPedidoEnum() != StatusPedidoEnum.FINALIZADO){
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Pedido não finalizado");
            }
            //ver se ja foi avaliado
            if (prestador.getAvaliacoes().stream().anyMatch(a -> a.getCodigoPedido().equals(pedido.getCodigoPedido()))){
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, String.format("Prestador %s já foi avaliado", prestador.getNome()));
            }
            if (prestadorEstrelasDto.getEstrelas() < 0 || prestadorEstrelasDto.getEstrelas() > 5){
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Estrelas devem ser entre 0 e 5");
            }
            AvaliacaoPrestador avaliacaoPrestador = prestadorService.criarAvaliacaoPrestador(pedido, prestador, prestadorEstrelasDto.getEstrelas(), prestadorEstrelasDto.getComentario(), cliente);
            prestador.getAvaliacoes().add(avaliacaoPrestador);
            prestador.atualizarEstrelas(prestador, prestadorEstrelasDto.getEstrelas());
            prestadoresAvaliados.add(prestador);
        }
        //salve os prestadores avaliados
        prestadorRepository.saveAll(prestadoresAvaliados);
    }

    public List<AvaliacaoPrestador> buscarAvaliacaoPorPrestador(Long idPrestador){
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Prestador prestadorLogado = prestadorRepository.findByEmail(emailUsuario).orElse(null);
        if (prestadorLogado == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "O usuário precisa estar logado");
        }

        Prestador prestador = prestadorService.buscarPorId(idPrestador);
        if (!prestador.getEmpresa().getCnpj().equals(prestadorLogado.getEmpresa().getCnpj())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Prestador não pertence a sua empresa");
        }

        return prestador.getAvaliacoes();
    }

    public List<AvaliacaoPrestador> buscarAvaliacaoPorCliente(String cpfCliente){
        Cliente cliente = clienteService.buscarPorCpf(cpfCliente);
        return avaliacaoRepository.findByClienteId(cliente.getId());
    }

    public List<AvaliacaoPrestador> buscarTodasAvaliacoes(){
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Prestador prestador = prestadorRepository.findByEmail(emailUsuario).orElse(null);
        if (emailUsuario == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "O usuário precisa estar logado");
        }
        Empresa empresa = prestador.getEmpresa();
        return avaliacaoRepository.findByCnpjEmpresa(empresa.getCnpj());
    }


    public List<AvaliacaoPrestador> buscarAvaliacaoPrestador(){
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Prestador prestador = prestadorRepository.findByEmail(emailUsuario).orElse(null);
        return prestador.getAvaliacoes();
    }
}
