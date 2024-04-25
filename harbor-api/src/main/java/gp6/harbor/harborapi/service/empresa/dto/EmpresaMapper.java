package gp6.harbor.harborapi.service.empresa.dto;


import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.endereco.Endereco;
import gp6.harbor.harborapi.service.endereco.dto.EnderecoMapper;
import jakarta.validation.Valid;

public class EmpresaMapper {

    public static Empresa toEntity(@Valid EmpresaCriacaoDto dto){
        if (dto == null){
            return null;
        }

        Empresa empresa = new Empresa();
        empresa.setRazaoSocial(dto.getRazaoSocial());
        empresa.setNomeFantasia(dto.getNomeFantasia());
        empresa.setCnpj(dto.getCnpj());

        // Mapear o EnderecoCriacaoDto para Endereco
        Endereco endereco = EnderecoMapper.toEntity(dto.getEndereco());

        // Definir o endereco na empresa
        empresa.setEndereco(endereco);


        // Mapear o CargoCriacaoDto para Prestador



        return empresa;
    }

}
