package gp6.harbor.harborapi.dto.empresa;

import gp6.harbor.harborapi.dto.endereco.EnderecoMapper;
import gp6.harbor.harborapi.entity.Empresa;
import gp6.harbor.harborapi.entity.Endereco;

public class EmpresaMapper {

    public static Empresa toEntity(EmpresaCriacaoDto dto){
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

        return empresa;
    }

}
