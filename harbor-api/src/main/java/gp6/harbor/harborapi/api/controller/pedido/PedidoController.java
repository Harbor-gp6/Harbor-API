package gp6.harbor.harborapi.api.controller.pedido;

import gp6.harbor.harborapi.domain.pedido.Pedido;
import gp6.harbor.harborapi.domain.pedido.PedidoProduto;
import gp6.harbor.harborapi.domain.pedido.repository.PedidoRepository;
import gp6.harbor.harborapi.domain.produto.Produto;
import gp6.harbor.harborapi.domain.produto.repository.ProdutoRepository;
import gp6.harbor.harborapi.service.empresa.dto.EmpresaListagemDto;
import gp6.harbor.harborapi.service.empresa.dto.EmpresaMapper;
import gp6.harbor.harborapi.service.pedido.dto.PedidoCriacaoDto;
import gp6.harbor.harborapi.service.pedido.dto.PedidoListagemDto;
import gp6.harbor.harborapi.service.pedido.dto.PedidoMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;

    @PostMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<PedidoListagemDto> criarPedido(@RequestBody @Valid PedidoCriacaoDto pedido) {

        List<PedidoProduto> listaProdutos = new ArrayList<>();
        for (Integer produtoId : pedido.getListaProdutoIds()) {
            Optional<Produto> produto = produtoRepository.findById(produtoId);

            if (produto != null) {
                PedidoProduto pedidoProduto = new PedidoProduto();
                pedidoProduto.setProduto(produto.get());
                // Defina a quantidade conforme necessário
                pedidoProduto.setQuantidade(1); // Ou obtenha do DTO, se aplicável
                listaProdutos.add(pedidoProduto);
            }
        }


        Pedido novoPedido = PedidoMapper.toEntity(pedido);

        Pedido pedidoSalvo = pedidoRepository.save(novoPedido);

        PedidoListagemDto listagemDto = PedidoMapper.toDto(pedidoSalvo);
        return ResponseEntity.status(201).body(listagemDto);
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<Pedido>> listarPedidos() {
        List<Pedido> pedidos = pedidoRepository.findAll();
        return ResponseEntity.status(200).body(pedidos);
    }

}
