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

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;



@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;


    @GetMapping
    @SecurityRequirement(name = "Bearer")
<<<<<<< Updated upstream
    public ResponseEntity<List<ClienteListagemDto>> listar(){
        List<Cliente> clientes = clienteService.buscarTodos();
=======
    public ResponseEntity<List<ClienteListagemDto>> listar() {
        List<Cliente> clientes = clienteRepository.findAll();
>>>>>>> Stashed changes

        if (clientes.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        List<ClienteListagemDto> listaAuxiliar = ClienteMapper.toDto(clientes);
        return ResponseEntity.status(200).body(listaAuxiliar);
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
<<<<<<< Updated upstream
    public ResponseEntity<ClienteListagemDto> buscarPorId(@PathVariable int id){
        ClienteListagemDto dto = ClienteMapper.toDto(clienteService.buscarPorId(id));
=======
    public ResponseEntity<ClienteListagemDto> buscarPorId(@PathVariable int id) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(id);

        if (clienteOpt.isEmpty()) {
            return ResponseEntity.status(404).build();

        }

        ClienteListagemDto dto = ClienteMapper.toDto(clienteOpt.get());
>>>>>>> Stashed changes
        return ResponseEntity.status(200).body(dto);

    }

    @GetMapping("/nome")
    @SecurityRequirement(name = "Bearer")
<<<<<<< Updated upstream
    public ResponseEntity<List<ClienteListagemDto>> buscarPorNome(@RequestParam String nome){
        List<Cliente> clientes = clienteService.buscarPorNome(nome);
=======
    public ResponseEntity<List<ClienteListagemDto>> buscarPorId(@RequestParam String nome) {
        List<Cliente> clientes = clienteRepository.findByNomeContainsIgnoreCase(nome);
>>>>>>> Stashed changes

        if (clientes.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        List<ClienteListagemDto> listaAuxiliar = ClienteMapper.toDto(clientes);

        return ResponseEntity.status(200).body(listaAuxiliar);
    }

    @PostMapping
<<<<<<< Updated upstream
    public ResponseEntity<ClienteListagemDto> cadastrar(@RequestBody @Valid ClienteCriacaoDto novoCliente){
=======
    public ResponseEntity<ClienteListagemDto> cadastrar(@RequestBody @Valid ClienteCriacaoDto novoCliente) {
        if (existePorCpf(novoCliente.getCpf())) {
            return ResponseEntity.status(409).build();
        }

>>>>>>> Stashed changes
        Cliente cliente = ClienteMapper.toEntity(novoCliente);
        Cliente clienteSalvo = clienteService.cadastrar(cliente);
        ClienteListagemDto listagemDto = ClienteMapper.toDto(clienteSalvo);

        return ResponseEntity.status(201).body(listagemDto);
    }


    @PutMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ClienteListagemDto> atualizarCliente(
            @PathVariable int id,
            @RequestBody @Valid ClienteCriacaoDto clienteAtualizado) {

<<<<<<< Updated upstream
=======
        if (!clienteRepository.existsById(id)) {
            return ResponseEntity.status(404).build();
        }


>>>>>>> Stashed changes
        Cliente cliente = ClienteMapper.toEntity(clienteAtualizado);
        cliente.setId(id);
        Cliente clienteSalvo = clienteService.atualizar(cliente);
        ClienteListagemDto listagemDto = ClienteMapper.toDto(clienteSalvo);


        return ResponseEntity.status(200).body(listagemDto);
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
<<<<<<< Updated upstream
    public ResponseEntity<Void> deletar(@PathVariable int id){
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
=======
    public ResponseEntity<Cliente> buscaPorId(@PathVariable @Valid int id) {
        if (clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(404).build();
    }

    @CrossOrigin("*")
    @PatchMapping(value = "/foto/{id}", consumes = {"image/jpeg", "image/png"})
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> patchFoto(@PathVariable int id, @RequestBody byte[] novaFoto) {
        if (!clienteRepository.existsById(id)) {
            return ResponseEntity.status(404).build();
        }

        clienteRepository.setFoto(id, novaFoto);
        return ResponseEntity.status(200).build();
    }

    @GetMapping(value = "/foto/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<byte[]> getFoto(@PathVariable int id) {
        if (!clienteRepository.existsById(id)) {
            return ResponseEntity.status(404).build();
        }

        byte[] foto = clienteRepository.getFoto(id);

        return ResponseEntity.status(200).header("content-disposition",
                "attachment; filename=\"foto-cliente.jpg\"").body(foto);
    }

    public boolean existePorCpf(String cpf){
        if (clienteRepository.existsByCpf(cpf)){
            return true;
        }
        return false;
>>>>>>> Stashed changes
    }
}
