package gp6.harbor.harborapi.dto.empresa.dto;

import gp6.harbor.harborapi.domain.empresa.Empresa;
import org.modelmapper.ModelMapper;

public class EmpresaMapperService {
    private final ModelMapper modelMapper;

    public EmpresaMapperService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public EmpresaCriacaoDto convertToDto(Empresa empresa) {
        return modelMapper.map(empresa, EmpresaCriacaoDto.class);
    }

    public EmpresaListagemDto convertToDtoListagem(Empresa empresa) {
        return modelMapper.map(empresa, EmpresaListagemDto.class);
    }

    public Empresa convertToEntity(EmpresaCriacaoDto enderecoCriacaoDto) {
        return modelMapper.map(enderecoCriacaoDto, Empresa.class);
    }
}
