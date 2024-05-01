package gp6.harbor.harborapi.api.controller.empresa;

import gp6.harbor.harborapi.domain.cliente.Cliente;
import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.empresa.repository.EmpresaRepository;
import gp6.harbor.harborapi.domain.endereco.Endereco;
import gp6.harbor.harborapi.domain.endereco.repository.EnderecoRepository;

import gp6.harbor.harborapi.service.empresa.dto.EmpresaCriacaoDto;
import gp6.harbor.harborapi.service.empresa.dto.EmpresaListagemDto;
import gp6.harbor.harborapi.service.empresa.dto.EmpresaMapper;
import gp6.harbor.harborapi.service.endereco.dto.EnderecoListagemDto;
import gp6.harbor.harborapi.service.endereco.dto.EnderecoMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @PostMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<EmpresaListagemDto> cadastrar(@RequestBody @Valid EmpresaCriacaoDto novaEmpresaDto){
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
        novaEmpresa.setDataCriacao(LocalDate.now());

        // Salvar empresa
        Empresa empresaSalva = empresaRepository.save(novaEmpresa);
        EmpresaListagemDto listagemDto = EmpresaMapper.toDto(empresaSalva);


        return ResponseEntity.status(201).body(listagemDto);
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<EmpresaListagemDto>> listar(){
        List<Empresa> empresas = empresaRepository.findAll();

        if (empresas.isEmpty()){
            return ResponseEntity.status(204).build();
        }

        List<EmpresaListagemDto> listaAuxiliar = EmpresaMapper.toDto(empresas);
        return ResponseEntity.status(200).body(listaAuxiliar);
    }


    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Empresa> inativarEmpresa(@PathVariable @Valid int id){
        if (empresaRepository.existsById(id)){
            Optional<Empresa> empresaOptional = empresaRepository.findById(id);
            empresaOptional.get().setDataInativacao(LocalDate.now());
            empresaRepository.save(empresaOptional.get());

            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(404).build();
    }

    public boolean existePorCnpj(String cnpj){
        return existePorCnpj(cnpj);
    }



    // TODO: Criar metodo de busca por CNPJ
    // TODO: Criar metodo de busca por id
    // TODO: Criar metodo de atualizar por id (tem que poder atualizar o endereco por aqui)
}