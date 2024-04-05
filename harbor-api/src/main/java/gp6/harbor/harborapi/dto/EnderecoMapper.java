package gp6.harbor.harborapi.dto;

import gp6.harbor.harborapi.entity.Cliente;
import gp6.harbor.harborapi.entity.Endereco;

import java.util.List;

public class EnderecoMapper {

    public static Endereco toEntity(EnderecoCriacaoDto EnderecoDto){
        if (EnderecoDto == null){
            return null;
        }

        Endereco endereco = new Endereco();

        endereco.setBairro(EnderecoDto.getBairro());
        endereco.setLogradouro(EnderecoDto.getLogradouro());
        endereco.setCidade(EnderecoDto.getCidade());
        endereco.setEstado(EnderecoDto.getEstado());
        endereco.setNumero(EnderecoDto.getNumero());
        endereco.setCep(EnderecoDto.getCep());
        endereco.setComplemento(EnderecoDto.getComplemento());

        return endereco;
    }

    public static EnderecoListagemDto toDto(Endereco entity){
        if (entity == null) return null;

        EnderecoListagemDto listagemDto = new EnderecoListagemDto();
        listagemDto.setId(entity.getId());
        listagemDto.setLogradouro(entity.getLogradouro());
        listagemDto.setCidade(entity.getCidade());
        listagemDto.setEstado(entity.getEstado());
        listagemDto.setNumero(entity.getNumero());
        listagemDto.setCep(entity.getCep());
        listagemDto.setComplemento(entity.getComplemento());
        listagemDto.setCep(entity.getCep());

        return listagemDto;

    }

    public static List<EnderecoListagemDto> toDto(List<Endereco> entities){
        return entities.stream().map(EnderecoMapper::toDto).toList();
    }
}
