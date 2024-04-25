package gp6.harbor.harborapi.api.controller.empresa;

import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.empresa.repository.EmpresaRepository;
import gp6.harbor.harborapi.domain.endereco.Endereco;
import gp6.harbor.harborapi.domain.endereco.repository.EnderecoRepository;

import gp6.harbor.harborapi.service.empresa.dto.EmpresaCriacaoDto;
import gp6.harbor.harborapi.service.empresa.dto.EmpresaMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @PostMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Empresa> cadastrar(@RequestBody @Valid EmpresaCriacaoDto novaEmpresaDto){
        // Mapear DTO para entidade
        Empresa novaEmpresa = EmpresaMapper.toEntity(novaEmpresaDto);

        // Verificar se já existe empresa com o mesmo CNPJ
        if (empresaRepository.existsByCnpj(novaEmpresa.getCnpj())){
            return ResponseEntity.status(409).build();
        }

        // Salvar endereço da empresa
        Endereco novoEndereco = novaEmpresa.getEndereco();
        Endereco enderecoSalvo = enderecoRepository.save(novoEndereco);

        // Associar endereço salvo à empresa
        novaEmpresa.setEndereco(enderecoSalvo);

        // Salvar empresa
        Empresa empresaSalva = empresaRepository.save(novaEmpresa);

        return ResponseEntity.status(201).body(empresaSalva);
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<Empresa>> listar(){
        List<Empresa> empresas = empresaRepository.findAll();

        if (empresas.isEmpty()){
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(empresas);
    }

    // TODO: Criar metodo de listagem por CNPJ
    // TODO: Criar metodo de atualizar por CNPJ




    public boolean existePorCnpj(String cnpj){
        return existePorCnpj(cnpj);
    }
}
