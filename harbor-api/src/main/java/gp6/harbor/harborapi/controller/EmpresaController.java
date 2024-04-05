package gp6.harbor.harborapi.controller;

import gp6.harbor.harborapi.dto.EmpresaCriacaoDto;
import gp6.harbor.harborapi.entity.Cliente;
import gp6.harbor.harborapi.repository.EmpresaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gp6.harbor.harborapi.entity.Empresa;
import gp6.harbor.harborapi.repository.EmpresaRepository;

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


    public boolean existePorCnpj(String cnpj){
        return existePorCnpj(cnpj);
    }
}
