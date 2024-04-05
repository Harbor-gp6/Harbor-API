package gp6.harbor.harborapi.dto;

import gp6.harbor.harborapi.entity.Cliente;

import java.util.List;

public class ClienteMapper {
    public static Cliente toEntity(ClienteCriacaoDto dto){
        if (dto == null){
            return null;
        }
        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setSobrenome(dto.getSobrenome());
        cliente.setTelefone(dto.getTelefone());
        cliente.setCpf(dto.getCpf());

        return cliente;
    }

    public static ClienteListagemDto toDto(Cliente entity){
        if (entity == null) return null;

        ClienteListagemDto listagemDto = new ClienteListagemDto();
        listagemDto.setId(entity.getId());
        listagemDto.setNome(entity.getNome());
        listagemDto.setSobrenome(entity.getSobrenome());
        listagemDto.setTelefone(entity.getTelefone());
        listagemDto.setCpf(entity.getCpf());
        listagemDto.setDataCriacao(entity.getDataCriacao());

        return listagemDto;
    }

    public static List<ClienteListagemDto> toDto(List<Cliente> entities){
        return entities.stream().map(ClienteMapper::toDto).toList();
    }




}
