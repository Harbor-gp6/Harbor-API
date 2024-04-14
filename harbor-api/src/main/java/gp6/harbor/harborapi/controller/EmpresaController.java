package gp6.harbor.harborapi.controller;

import gp6.harbor.harborapi.dto.EmpresaCriacaoDto;
import gp6.harbor.harborapi.dto.EnderecoListagemDto;
import gp6.harbor.harborapi.dto.EnderecoMapper;
import gp6.harbor.harborapi.entity.Cliente;
import gp6.harbor.harborapi.repository.EmpresaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import gp6.harbor.harborapi.entity.Empresa;
import gp6.harbor.harborapi.repository.EmpresaRepository;

import java.util.List;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaRepository empresaRepository;

    @PostMapping
    public ResponseEntity<Empresa> cadastrar(@RequestBody @Valid Empresa novaEmpresa){
            if (existePorCnpj(novaEmpresa.getCnpj())){
                return ResponseEntity.status(409).build();
            }

        Empresa empresaSalva = empresaRepository.save(novaEmpresa);
        return ResponseEntity.status(201).body(empresaSalva);

    }

    @GetMapping
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
