package gp6.harbor.harborapi.service.pedido.dto;

import gp6.harbor.harborapi.api.controller.produto.ProdutoController;
import gp6.harbor.harborapi.api.controller.servico.ServicoController;
import gp6.harbor.harborapi.domain.cliente.Cliente;
import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.endereco.Endereco;
import gp6.harbor.harborapi.domain.pedido.Pedido;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import gp6.harbor.harborapi.domain.produto.Produto;
import gp6.harbor.harborapi.domain.produto.repository.ProdutoRepository;
import gp6.harbor.harborapi.domain.servico.Servico;
import gp6.harbor.harborapi.domain.servico.repository.ServicoRepository;
import gp6.harbor.harborapi.service.cliente.dto.ClienteMapper;
import gp6.harbor.harborapi.service.empresa.dto.EmpresaCriacaoDto;
import gp6.harbor.harborapi.service.empresa.dto.EmpresaListagemDto;
import gp6.harbor.harborapi.service.endereco.dto.EnderecoMapper;
import gp6.harbor.harborapi.service.prestador.dto.PrestadorListagemDto;
import gp6.harbor.harborapi.service.prestador.dto.PrestadorMapper;
import gp6.harbor.harborapi.service.produto.dto.ProdutoMapper;
import gp6.harbor.harborapi.service.servico.dto.ServicoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class PedidoMapper {

    public static Pedido toEntity(PedidoCriacaoDto dto){
        if (dto == null){
            return null;
        }
        Pedido pedido = new Pedido();

        pedido.setCliente(dto.getCliente());

        pedido.setListaProduto(ProdutoController.buscarPorListaDeIdsEntidade(dto.getListaProdutoIds()));
        pedido.setListaServico(ServicoController.buscarPorListaDeIdsEntidade(dto.getListaServicoIds()));

        pedido.setDataAgendamento(dto.getDataAgendamento());
        pedido.setObservacao(dto.getObservacao());

        return pedido;

    }

    public static PedidoListagemDto toDto(Pedido entity) {
        if (entity == null) {
            return null;
        }
        PedidoListagemDto pedidoListagemDto = new PedidoListagemDto();

        pedidoListagemDto.setCliente(entity.getCliente().getNome() + " " + entity.getCliente().getSobrenome());

        pedidoListagemDto.setListaServico(ServicoMapper.toDto(entity.getListaServico()));
        pedidoListagemDto.setListaProduto(ProdutoMapper.toDto(entity.getListaProduto()));

        pedidoListagemDto.setPrestador(PrestadorMapper.toDto(entity.getPrestador()));

        pedidoListagemDto.setDataAgendamento(entity.getDataAgendamento());
        pedidoListagemDto.setObservacao(entity.getObservacao());

        return pedidoListagemDto;
    }
}

