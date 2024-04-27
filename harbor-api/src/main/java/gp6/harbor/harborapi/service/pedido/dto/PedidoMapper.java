package gp6.harbor.harborapi.service.pedido.dto;

import gp6.harbor.harborapi.domain.cliente.Cliente;
import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.endereco.Endereco;
import gp6.harbor.harborapi.domain.pedido.Pedido;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import gp6.harbor.harborapi.service.cliente.dto.ClienteMapper;
import gp6.harbor.harborapi.service.empresa.dto.EmpresaCriacaoDto;
import gp6.harbor.harborapi.service.empresa.dto.EmpresaListagemDto;
import gp6.harbor.harborapi.service.endereco.dto.EnderecoMapper;
import gp6.harbor.harborapi.service.prestador.dto.PrestadorListagemDto;
import gp6.harbor.harborapi.service.prestador.dto.PrestadorMapper;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PedidoMapper {
    public static Pedido toEntity(@Valid PedidoCriacaoDto dto){
        if (dto == null){
            return null;
        }
        Pedido pedido = new Pedido();

        Cliente cliente = new Cliente();
        cliente.setNome(dto.getCliente().getNome());
        cliente.setSobrenome(dto.getCliente().getSobrenome());
        cliente.setTelefone(dto.getCliente().getTelefone());
        cliente.setCpf(dto.getCliente().getCpf());

        pedido.setCliente(dto.getCliente());


        pedido.setListaProduto(dto.getListaProduto());
        pedido.setListaServico(dto.getListaServico());


        pedido.setDataAgendamento(dto.getDataAgendamento());
        pedido.setObservacao(dto.getObservacao());

        return pedido;
    }


    public static PedidoListagemDto toDto(Pedido entity){
        if (entity == null) return null;

        PedidoListagemDto listagemDto = new PedidoListagemDto();
        listagemDto.setCliente(entity.getCliente().getNome() + " " + entity.getCliente().getSobrenome());
        listagemDto.setListaProduto(entity.getListaProduto());
        listagemDto.setListaServico(entity.getListaServico());
        listagemDto.setDataAgendamento(entity.getDataAgendamento());
        listagemDto.setObservacao(entity.getObservacao());

        return listagemDto;
    }

    public static List<PedidoListagemDto> toDto(List<Pedido> entities){
        return entities.stream().map(PedidoMapper::toDto).toList();
    }

}
