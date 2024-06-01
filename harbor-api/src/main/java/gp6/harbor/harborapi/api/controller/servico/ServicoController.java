package gp6.harbor.harborapi.api.controller.servico;

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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/servicos")
@RequiredArgsConstructor
public class ServicoController {

    private final ServicoRepository servicoRepository2;
    private final ServicoService servicoService;
    private final EmpresaRepository empresaRepository;

    @PostMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ServicoListagemDto> cadastrar(@RequestBody @Valid ServicoCriacaoDto novoServico) {

        Optional<Empresa> empresaOptional = empresaRepository.findByCnpj(novoServico.getCNPJempresa());

        if (empresaOptional.isEmpty()){
            return  ResponseEntity.status(404).build();
        }

        Empresa empresa = empresaOptional.get();

        Servico servico = ServicoMapper.toEntity(novoServico);
        servico.setEmpresa(empresa);

        Servico servicoSalvo = servicoRepository2.save(servico);

        ServicoListagemDto listagemDto = ServicoMapper.toDto(servicoSalvo);

        return ResponseEntity.status(201).body(listagemDto);
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ServicoListagemDto>> listar(){
        List<Servico> servicos = servicoRepository2.findAll();

        if (servicos.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        List<ServicoListagemDto> listaAuxiliar = ServicoMapper.toDto(servicos);
        return ResponseEntity.status(200).body(listaAuxiliar);
    }

    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<ServicoListagemDto>> buscarPorEmpresa(@PathVariable Integer empresaId){
        Optional<Empresa> empresaOptional = empresaRepository.findById(empresaId);

        if (empresaOptional.isEmpty()){
            return ResponseEntity.status(404).build();
        }
        List<Servico> servicosPorEmpresa = servicoService.buscaPorEmpresa(empresaOptional.get());
        if (servicosPorEmpresa.isEmpty()) {
            return ResponseEntity.status(404).build();
        }
        List<ServicoListagemDto> listaAuxiliar = ServicoMapper.toDto(servicosPorEmpresa);
        return ResponseEntity.status(200).body(listaAuxiliar);
    }

    //TODO: Criar listagem de servicos
    //TODO: Criar busca de servicos por id
    //TODO: Criar alteracao de servicos por id
    //TODO: Criar delete de servicos por id

}
