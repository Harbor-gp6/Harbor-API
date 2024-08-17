package gp6.harbor.harborapi.api.controller.servico;

import java.util.List;
import java.util.Optional;

import gp6.harbor.harborapi.dto.servico.dto.ServicoAtualizacaoDto;
import gp6.harbor.harborapi.service.empresa.EmpresaService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.empresa.repository.EmpresaRepository;
import gp6.harbor.harborapi.domain.servico.Servico;
import gp6.harbor.harborapi.domain.servico.repository.ServicoRepository;
import gp6.harbor.harborapi.dto.servico.dto.ServicoCriacaoDto;
import gp6.harbor.harborapi.dto.servico.dto.ServicoListagemDto;
import gp6.harbor.harborapi.dto.servico.dto.ServicoMapper;
import gp6.harbor.harborapi.service.servico.ServicoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/servicos")
@RequiredArgsConstructor
public class ServicoController {

    private final ServicoService servicoService;
    private final EmpresaService empresaService;

    @PostMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ServicoListagemDto> cadastrar(@RequestBody @Valid ServicoCriacaoDto novoServico) {

        Empresa empresa = empresaService.buscarPorCnpj(novoServico.getCnpjEmpresa());

        Servico servico = ServicoMapper.toEntity(novoServico);
        servico.setEmpresa(empresa);

        Servico servicoSalvo = servicoService.salvar(servico);

        ServicoListagemDto listagemDto = ServicoMapper.toDto(servicoSalvo);

        return ResponseEntity.status(201).body(listagemDto);
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ServicoListagemDto>> listar(){
        List<Servico> servicos = servicoService.listar();

        if (servicos.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        List<ServicoListagemDto> listaAuxiliar = ServicoMapper.toDto(servicos);
        return ResponseEntity.status(200).body(listaAuxiliar);
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ServicoListagemDto> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(ServicoMapper.toDto(servicoService.buscaPorId(id)));
    }

    @GetMapping("/empresa/{empresaId}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ServicoListagemDto>> buscarPorEmpresa(@PathVariable Integer empresaId){
        Empresa empresa = empresaService.buscarPorId(empresaId);

        List<Servico> servicosPorEmpresa = servicoService.buscaPorEmpresa(empresa);
        if (servicosPorEmpresa.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<ServicoListagemDto> listaAuxiliar = ServicoMapper.toDto(servicosPorEmpresa);
        return ResponseEntity.status(200).body(listaAuxiliar);
    }

    @PutMapping("/{empresaId}/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ServicoListagemDto> atualizar(@PathVariable int empresaId, @PathVariable int id, @RequestBody @Valid ServicoAtualizacaoDto servicoAtualizado) {
        return ResponseEntity.ok(ServicoMapper.toDto(servicoService.atualizar(empresaId, id, ServicoMapper.toEntity(servicoAtualizado))));
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> deletar(@PathVariable int id) {
        servicoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @CrossOrigin("*")
    @PatchMapping(value = "/foto/{id}", consumes = {"image/jpeg", "image/png"})
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> patchFoto(@PathVariable Integer id,
                                          @RequestBody byte[] novaFoto) {
        if (!servicoService.existePorId(id)) {
            return ResponseEntity.status(404).build();
        }

        servicoService.setFoto(id, novaFoto);
        return ResponseEntity.status(200).build();
    }
    @GetMapping(value = "/foto/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<byte[]> getFoto(@PathVariable Integer id) {
        if (!servicoService.existePorId(id)) {
            return ResponseEntity.status(404).build();
        }

        byte[] foto = servicoService.getFoto(id);

        return ResponseEntity.status(200).header("content-disposition",
                "attachment; filename=\"foto-servico.jpg\"").body(foto);
    }
}
