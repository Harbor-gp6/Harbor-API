package gp6.harbor.harborapi.api.controller.prestador;

import gp6.harbor.harborapi.domain.cargo.Cargo;
import gp6.harbor.harborapi.domain.cargo.repository.CargoRepository;
import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.empresa.repository.EmpresaRepository;
import gp6.harbor.harborapi.domain.endereco.repository.EnderecoRepository;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import gp6.harbor.harborapi.domain.prestador.repository.PrestadorRepository;
import gp6.harbor.harborapi.service.cargo.dto.CargoMapper;
import gp6.harbor.harborapi.service.empresa.dto.EmpresaMapper;
import gp6.harbor.harborapi.service.prestador.dto.PrestadorCriacaoDto;
import gp6.harbor.harborapi.service.prestador.dto.PrestadorListagemDto;
import gp6.harbor.harborapi.service.prestador.dto.PrestadorMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import gp6.harbor.harborapi.service.usuario.UsuarioService;
import gp6.harbor.harborapi.service.usuario.autenticacao.dto.UsuarioLoginDto;
import gp6.harbor.harborapi.service.usuario.autenticacao.dto.UsuarioTokenDto;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class PrestadorController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PrestadorRepository prestadorRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private CargoRepository cargoRepository;

    @PostMapping
    public ResponseEntity<Void> criar(@RequestBody @Valid PrestadorCriacaoDto usuarioCriacaoDto) {
        if (existePorCpf(usuarioCriacaoDto.getCpf()) ||
            existePorEmail(usuarioCriacaoDto.getEmail())) {
            return ResponseEntity.status(409).build();
        }

        // Convertendo o DTO de empresa para entidade e salvando-a
        Empresa empresa = usuarioCriacaoDto.getEmpresa();

        Cargo cargo = usuarioCriacaoDto.getCargo();

        // Configurando a empresa para o novo prestador
        Prestador prestador = PrestadorMapper.toEntity(usuarioCriacaoDto);
        prestador.setEmpresa(empresa);
        prestador.setCargo(cargo);

        // Salvando o prestador
        prestadorRepository.save(prestador);

        usuarioCriacaoDto.setCargo(cargo);
        usuarioCriacaoDto.setEmpresa(empresa);

        this.usuarioService.criar(usuarioCriacaoDto);

        return ResponseEntity.status(201).build();
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioTokenDto> login(@RequestBody UsuarioLoginDto usuarioLoginDto) {
        UsuarioTokenDto usuarioTokenDto = this.usuarioService.autenticar(usuarioLoginDto);

        return ResponseEntity.status(200).body(usuarioTokenDto);
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<PrestadorListagemDto>> listar(){
        List<Prestador> prestadores = prestadorRepository.findAll();

        if (prestadores.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        List<PrestadorListagemDto> listaAuxiliar = PrestadorMapper.toDto(prestadores);
        return ResponseEntity.status(200).body(listaAuxiliar);
    }

    //TODO: Criar busca de Prestador por id
    //TODO: Criar busca de Prestador por cpf
    //TODO: Criar atualizacao de Prestador por id
    private boolean existePorCpf(String cpf){
        return prestadorRepository.existsByCpf(cpf);
    }

    private boolean existePorEmail(String email) {
        return prestadorRepository.existsByEmail(email);
    }
}