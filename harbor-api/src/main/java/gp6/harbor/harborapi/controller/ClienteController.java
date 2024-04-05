package gp6.harbor.harborapi.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import gp6.harbor.harborapi.entity.Cliente;
import gp6.harbor.harborapi.repository.ClienteRepository;

import javax.swing.*;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @PostMapping
    public ResponseEntity<Cliente> cadastrar(@RequestBody @Valid Cliente novoCliente){
        if (existePorCpf(novoCliente.getCpf())){
            return ResponseEntity.status(409).build();
        }
        Cliente clienteSalvo = clienteRepository.save(novoCliente);
        return ResponseEntity.status(201).body(clienteSalvo);
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listar(){
        List<Cliente> clientes = clienteRepository.findAll();

        if (clientes.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(200).body(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable int id){
        Optional<Cliente> clienteOpt = clienteRepository.findById(id);

        if (clienteOpt.isPresent()){
            return ResponseEntity.status(200).body(clienteOpt.get());
        }

        return ResponseEntity.status(404).body(clienteOpt.get());
    }

    @GetMapping("/nome")
    public ResponseEntity<List<Cliente>> buscarPorId(@RequestParam String nome){
        List<Cliente> clientes = clienteRepository.findByNomeContainsIgnoreCase(nome);

        if (clientes.isEmpty()){
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(clientes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Cliente> buscaPorId(@PathVariable @Valid int id){
        if (clienteRepository.existsById(id)){
            clienteRepository.deleteById(id);
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(404).build();
    }

    @PutMapping("{id}")
    public ResponseEntity<Cliente> atualizarCliente(
            @PathVariable int id,
            @RequestBody @Valid Cliente clienteAtualizado){

        if (!clienteRepository.existsById(id)){
            return ResponseEntity.status(404).build();
        }

        clienteAtualizado.setId(id);

        Cliente clienteSalvo = clienteRepository.save(clienteAtualizado);
            return ResponseEntity.status(200).body(clienteSalvo);
    }

    public boolean existePorCpf(String cpf){
        if (clienteRepository.existsByCpf(cpf)){
            return true;
        }
        return false;
    }
}
