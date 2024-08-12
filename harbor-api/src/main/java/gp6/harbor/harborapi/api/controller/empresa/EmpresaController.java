package gp6.harbor.harborapi.api.controller.empresa;

import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.empresa.repository.EmpresaRepository;
import gp6.harbor.harborapi.domain.endereco.Endereco;
import gp6.harbor.harborapi.domain.endereco.repository.EnderecoRepository;

import gp6.harbor.harborapi.dto.empresa.dto.EmpresaCriacaoDto;
import gp6.harbor.harborapi.dto.empresa.dto.EmpresaListagemDto;
import gp6.harbor.harborapi.dto.empresa.dto.EmpresaMapper;
import gp6.harbor.harborapi.service.empresa.EmpresaService;
import gp6.harbor.harborapi.service.endereco.EnderecoService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/empresas")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaService empresaService;

    private EnderecoService enderecoService;

    @Hidden
    @PostMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<EmpresaListagemDto> cadastrar(@RequestBody @Valid EmpresaCriacaoDto novaEmpresaDto){
        // Mapear DTO para entidade
        Empresa novaEmpresa = EmpresaMapper.toEntity(novaEmpresaDto);


        // Verificar se já existe empresa com o mesmo CNPJ
        if (empresaService.existePorCnpj(novaEmpresa.getCnpj())){
            return ResponseEntity.status(409).build();
        }

        // Salvar endereço da empresa
        Endereco novoEndereco = novaEmpresa.getEndereco();
        Endereco enderecoSalvo = enderecoService.cadastrar(novoEndereco);

        // Associar endereço salvo à empresa
        novaEmpresa.setEndereco(enderecoSalvo);
        novaEmpresa.setDataCriacao(LocalDate.now());

        // Salvar empresa
        Empresa empresaSalva = empresaService.cadastrar(novaEmpresa);
        EmpresaListagemDto listagemDto = EmpresaMapper.toDto(empresaSalva);


        return ResponseEntity.status(201).body(listagemDto);
    }

    @Hidden
    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<EmpresaListagemDto>> listar(){
        List<Empresa> empresas = empresaService.listar();

        if (empresas.isEmpty()){
            return ResponseEntity.status(204).build();
        }

        List<EmpresaListagemDto> listaAuxiliar = EmpresaMapper.toDto(empresas);
        return ResponseEntity.status(200).body(listaAuxiliar);
    }
    @Hidden
    @GetMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<EmpresaListagemDto> buscarPorId(@PathVariable Integer id){
        Empresa empresa = empresaService.buscarPorId(id);
        if (empresa == null){
            return ResponseEntity.status(404).build();
        }
        EmpresaListagemDto listaAuxiliar = EmpresaMapper.toDto(empresa);
        return ResponseEntity.status(200).body(listaAuxiliar);
    }
    @Hidden
    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> inativarEmpresa(@PathVariable @Valid int id) {
        if (empresaService.existePorId(id)) {
            empresaService.inativarEmpresa(id);
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
