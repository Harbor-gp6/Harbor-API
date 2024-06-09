package gp6.harbor.harborapi.service.pedido;

import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.pedido.Pedido;
import gp6.harbor.harborapi.domain.pedido.PedidoServico;
import gp6.harbor.harborapi.domain.pedido.repository.PedidoServicoRepository;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import gp6.harbor.harborapi.domain.servico.Servico;
import gp6.harbor.harborapi.service.prestador.PrestadorService;
import gp6.harbor.harborapi.service.servico.ServicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServicoService {

    private final PedidoServicoRepository pedidoServicoRepository;
    private final ServicoService servicoService;
    private final PrestadorService prestadorService;

    public PedidoServico adicionarServico(Servico servico, Pedido pedido) {
        PedidoServico pedidoServico = new PedidoServico();

        pedidoServico.setServico(servico);
        pedidoServico.setPedido(pedido);

        return pedidoServicoRepository.save(pedidoServico);
    }

    public List<PedidoServico> salvarTodos(List<PedidoServico> pedidoServicos) {
        return pedidoServicoRepository.saveAll(pedidoServicos);
    }

    public Map<String, Map<String, Integer>> criarMatrizDeServicos(Empresa empresa) {
        List<PedidoServico> pedidosServicos = pedidoServicoRepository.findAll();

        // Filtra pedidosServicos pela empresa
        pedidosServicos = pedidosServicos.stream()
                .filter(ps -> ps.getServico().getEmpresa().equals(empresa))
                .collect(Collectors.toList());

        // Coletando todos os prestadores e serviços da empresa
        List<Prestador> prestadores = prestadorService.buscarPorEmpresa(empresa);
        List<Servico> servicos = servicoService.buscaPorEmpresa(empresa);

        // Criando um mapa para contar quantos serviços cada prestador prestou
        Map<String, Map<String, Integer>> matrizMap = new HashMap<>();

        for (Servico servico : servicos) {
            matrizMap.put(servico.getDescricaoServico(), new HashMap<>());
            for (Prestador prestador : prestadores) {
                matrizMap.get(servico.getDescricaoServico()).put(prestador.getNome(), 0);
            }
        }

        // Populando o mapa com os dados reais
        for (PedidoServico ps : pedidosServicos) {
            String prestador = ps.getPedido().getPrestador().getNome();
            String servico = ps.getServico().getDescricaoServico();
            matrizMap.get(servico).put(prestador, matrizMap.get(servico).get(prestador) + 1);
        }

        return matrizMap;
    }
}