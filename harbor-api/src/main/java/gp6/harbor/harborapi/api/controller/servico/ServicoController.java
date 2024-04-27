package gp6.harbor.harborapi.api.controller.servico;

import gp6.harbor.harborapi.domain.cargo.Cargo;
import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.empresa.repository.EmpresaRepository;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import gp6.harbor.harborapi.domain.servico.Servico;
import gp6.harbor.harborapi.domain.servico.repository.ServicoRepository;
import gp6.harbor.harborapi.service.cargo.dto.CargoMapper;
import gp6.harbor.harborapi.service.empresa.dto.EmpresaMapper;
import gp6.harbor.harborapi.service.prestador.dto.PrestadorCriacaoDto;
import gp6.harbor.harborapi.service.prestador.dto.PrestadorListagemDto;
import gp6.harbor.harborapi.service.prestador.dto.PrestadorMapper;
import gp6.harbor.harborapi.service.servico.dto.ServicoCriacaoDto;
import gp6.harbor.harborapi.service.servico.dto.ServicoListagemDto;
import gp6.harbor.harborapi.service.servico.dto.ServicoMapper;
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

    private final ServicoRepository servicoRepository;
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

        Servico servicoSalvo = servicoRepository.save(servico);

        ServicoListagemDto listagemDto = ServicoMapper.toDto(servicoSalvo);

        return ResponseEntity.status(201).body(listagemDto);
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ServicoListagemDto>> listar(){
        List<Servico> servicos = servicoRepository.findAll();

        if (servicos.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        List<ServicoListagemDto> listaAuxiliar = ServicoMapper.toDto(servicos);
        return ResponseEntity.status(200).body(listaAuxiliar);
    }

    //TODO: Criar listagem de servicos
    //TODO: Criar busca de servicos por id
    //TODO: Criar alteracao de servicos por id
    //TODO: Criar delete de servicos por id




}
