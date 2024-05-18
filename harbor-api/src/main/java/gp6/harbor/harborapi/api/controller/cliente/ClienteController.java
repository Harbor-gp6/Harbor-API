package gp6.harbor.harborapi.api.controller.cliente;

import gp6.harbor.harborapi.domain.cliente.Cliente;
import gp6.harbor.harborapi.domain.cliente.repository.ClienteRepository;

import gp6.harbor.harborapi.dto.cliente.dto.ClienteCriacaoDto;
import gp6.harbor.harborapi.dto.cliente.dto.ClienteListagemDto;
import gp6.harbor.harborapi.dto.cliente.dto.ClienteMapper;
import gp6.harbor.harborapi.service.cliente.ClienteService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;



@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;



    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ClienteListagemDto>> listar(){
        List<Cliente> clientes = clienteService.buscarTodos();

        if (clientes.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        List<ClienteListagemDto> listaAuxiliar = ClienteMapper.toDto(clientes);
        return ResponseEntity.status(200).body(listaAuxiliar);
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ClienteListagemDto> buscarPorId(@PathVariable int id){
        ClienteListagemDto dto = ClienteMapper.toDto(clienteService.buscarPorId(id));
        return ResponseEntity.status(200).body(dto);

    }

    @GetMapping("/nome")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ClienteListagemDto>> buscarPorNome(@RequestParam String nome){
        List<Cliente> clientes = clienteService.buscarPorNome(nome);

        if (clientes.isEmpty()){
            return ResponseEntity.status(204).build();
        }

        List<ClienteListagemDto> listaAuxiliar = ClienteMapper.toDto(clientes);

        return ResponseEntity.status(200).body(listaAuxiliar);
    }

    @PostMapping
    public ResponseEntity<ClienteListagemDto> cadastrar(@RequestBody @Valid ClienteCriacaoDto novoCliente){
        Cliente cliente = ClienteMapper.toEntity(novoCliente);
        Cliente clienteSalvo = clienteService.cadastrar(cliente);
        ClienteListagemDto listagemDto = ClienteMapper.toDto(clienteSalvo);

        return ResponseEntity.status(201).body(listagemDto);
    }


    @PutMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ClienteListagemDto> atualizarCliente(
            @PathVariable int id,
            @RequestBody @Valid ClienteCriacaoDto clienteAtualizado){

        Cliente cliente = ClienteMapper.toEntity(clienteAtualizado);
        cliente.setId(id);
        Cliente clienteSalvo = clienteService.atualizar(cliente);
        ClienteListagemDto listagemDto = ClienteMapper.toDto(clienteSalvo);


        return ResponseEntity.status(200).body(listagemDto);
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> deletar(@PathVariable int id){
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
