package gp6.harbor.harborapi.api.controller.cliente;

import gp6.harbor.harborapi.domain.cliente.Cliente;
import gp6.harbor.harborapi.domain.cliente.repository.ClienteRepository;

import gp6.harbor.harborapi.service.cliente.dto.ClienteCriacaoDto;
import gp6.harbor.harborapi.service.cliente.dto.ClienteListagemDto;
import gp6.harbor.harborapi.service.cliente.dto.ClienteMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;



@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @PostMapping
    public ResponseEntity<ClienteListagemDto> cadastrar(@RequestBody @Valid ClienteCriacaoDto novoCliente){
        if (existePorCpf(novoCliente.getCpf())){
            return ResponseEntity.status(409).build();
        }

        Cliente cliente = ClienteMapper.toEntity(novoCliente);
        Cliente clienteSalvo = clienteRepository.save(cliente);
        ClienteListagemDto listagemDto = ClienteMapper.toDto(clienteSalvo);

        return ResponseEntity.status(201).body(listagemDto);
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ClienteListagemDto>> listar(){
        List<Cliente> clientes = clienteRepository.findAll();

        if (clientes.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        List<ClienteListagemDto> listaAuxiliar = ClienteMapper.toDto(clientes);
        return ResponseEntity.status(200).body(listaAuxiliar);
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ClienteListagemDto> buscarPorId(@PathVariable int id){
        Optional<Cliente> clienteOpt = clienteRepository.findById(id);

        if (clienteOpt.isEmpty()){
            return ResponseEntity.status(404).build();

        }

        ClienteListagemDto dto = ClienteMapper.toDto(clienteOpt.get());
        return ResponseEntity.status(200).body(dto);

    }

    @GetMapping("/nome")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ClienteListagemDto>> buscarPorId(@RequestParam String nome){
        List<Cliente> clientes = clienteRepository.findByNomeContainsIgnoreCase(nome);

        if (clientes.isEmpty()){
            return ResponseEntity.status(204).build();
        }

        List<ClienteListagemDto> listaAuxiliar = ClienteMapper.toDto(clientes);

        return ResponseEntity.status(200).body(listaAuxiliar);
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Cliente> buscaPorId(@PathVariable @Valid int id){
        if (clienteRepository.existsById(id)){
            clienteRepository.deleteById(id);
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(404).build();
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ClienteListagemDto> atualizarCliente(
            @PathVariable int id,
            @RequestBody @Valid ClienteCriacaoDto clienteAtualizado){

        if (!clienteRepository.existsById(id)){
            return ResponseEntity.status(404).build();
        }


        Cliente cliente = ClienteMapper.toEntity(clienteAtualizado);
        cliente.setId(id);
        Cliente clienteSalvo = clienteRepository.save(cliente);
        ClienteListagemDto listagemDto = ClienteMapper.toDto(clienteSalvo);


        return ResponseEntity.status(200).body(listagemDto);
    }

    public boolean existePorCpf(String cpf){
        if (clienteRepository.existsByCpf(cpf)){
            return true;
        }
        return false;
    }
}
