package gp6.harbor.harborapi.dto;

import gp6.harbor.harborapi.entity.Cliente;

import java.util.List;

public class EmpresaMapper {

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


    public static List<ClienteListagemDto> toDto(List<Cliente> entities){
        return entities.stream().map(ClienteMapper::toDto).toList();
    }
}
