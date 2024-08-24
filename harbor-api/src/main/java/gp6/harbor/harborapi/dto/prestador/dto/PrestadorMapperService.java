package gp6.harbor.harborapi.dto.prestador.dto;

import gp6.harbor.harborapi.domain.prestador.Prestador;
import gp6.harbor.harborapi.dto.prestador.dto.PrestadorFuncionarioCriacao;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;


@Service
public class PrestadorMapperService {

    private final ModelMapper modelMapper;

    public PrestadorMapperService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public PrestadorFuncionarioCriacao convertToDto(Prestador prestador) {
        return modelMapper.map(prestador, PrestadorFuncionarioCriacao.class);
    }

    public FuncionarioListagemDto convertToFuncionarioListagemDto(Prestador prestador) {
        return modelMapper.map(prestador, FuncionarioListagemDto.class);
    }

    public Prestador convertToEntity(PrestadorFuncionarioCriacao prestadorDto) {
        return modelMapper.map(prestadorDto, Prestador.class);
    }
}
