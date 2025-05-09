package gp6.harbor.harborapi.api.controller.empresa;

import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.endereco.Endereco;
import gp6.harbor.harborapi.dto.empresa.dto.EmpresaCriacaoDto;
import gp6.harbor.harborapi.dto.empresa.dto.EmpresaListagemDto;
import gp6.harbor.harborapi.dto.empresa.dto.EmpresaMapper;
import gp6.harbor.harborapi.service.email.EmailService;
import gp6.harbor.harborapi.service.empresa.EmpresaService;
import gp6.harbor.harborapi.service.endereco.EnderecoService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/empresas")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaService empresaService;
    private final EnderecoService enderecoService;
    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> sendEmail(@RequestParam String to,
                            @RequestParam String subject,
                            @RequestParam String body) {

            emailService.sendEmailV2(to, subject, body);
        return ResponseEntity.status(200).build();
    }

    @PostMapping
    public ResponseEntity<EmpresaListagemDto> cadastrar(@RequestBody @Valid EmpresaCriacaoDto novaEmpresaDto){
        // Mapear DTO para entidade
        Empresa novaEmpresa = EmpresaMapper.toEntity(novaEmpresaDto);

        // Verificar se já existe empresa com o mesmo CNPJ
        if (empresaService.existePorCnpj(novaEmpresa.getCnpj())){
            return ResponseEntity.status(409).build();
        }

        // Salvar endereço da empresa
        Endereco novoEndereco = novaEmpresa.getEndereco();
        Endereco enderecoSalvo = enderecoService.cadastrar(novoEndereco);

        // Associar endereço salvo à empresa
        novaEmpresa.setEndereco(enderecoSalvo);
        novaEmpresa.setDataCriacao(LocalDate.now());

        // Salvar empresa
        Empresa empresaSalva = empresaService.cadastrar(novaEmpresa);
        EmpresaListagemDto listagemDto = EmpresaMapper.toDto(empresaSalva);

        return ResponseEntity.status(201).body(listagemDto);
    }

    @Hidden
    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<EmpresaListagemDto>> listar(){
        List<Empresa> empresas = empresaService.listar();

        if (empresas.isEmpty()){
            return ResponseEntity.status(204).build();
        }

        List<EmpresaListagemDto> listaAuxiliar = EmpresaMapper.toDto(empresas);
        return ResponseEntity.status(200).body(listaAuxiliar);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<EmpresaListagemDto>> buscarPorRazaoSocial(@RequestBody String razaoSocial){
        List<Empresa> empresas = empresaService.buscarPorRazaoSocial(razaoSocial);
        if (empresas.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        List<EmpresaListagemDto> empresasEncontradas = EmpresaMapper.toDto(empresas);
        return ResponseEntity.status(200).body(empresasEncontradas);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<EmpresaListagemDto> buscarPorSlug(@PathVariable String slug) {
        Empresa empresa = empresaService.buscarPorSlug(slug);
        if (empresa == null) {
            return ResponseEntity.status(404).build();
        }

        EmpresaListagemDto empresaEncontrada = EmpresaMapper.toDto(empresa);
        return ResponseEntity.status(200).body(empresaEncontrada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpresaListagemDto> buscarPorId(@PathVariable Integer id){
        Empresa empresa = empresaService.buscarPorId(id);
        if (empresa == null){
            return ResponseEntity.status(404).build();
        }
        EmpresaListagemDto listaAuxiliar = EmpresaMapper.toDto(empresa);
        return ResponseEntity.status(200).body(listaAuxiliar);
    }

    @Hidden
    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> inativarEmpresa(@PathVariable @Valid int id) {
        if (empresaService.existePorId(id)) {
            empresaService.inativarEmpresa(id);
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(404).build();
    }

    @CrossOrigin("*")
    @PatchMapping(value = "/foto")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> patchFoto(@RequestBody String novaFoto) {
        empresaService.setFoto(novaFoto);
        return ResponseEntity.status(200).build();
    }

    @GetMapping(value = "/obter-foto/{id}")
    public ResponseEntity<String> getFoto(@PathVariable @Valid int id) {

        String foto = empresaService.getFoto(id);

        return ResponseEntity.status(200).body(foto);
    }

    @CrossOrigin("*")
    @PatchMapping(value = "/banner")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> patchBanner(@RequestBody String novaFoto) {
        empresaService.setBanner(novaFoto);
        return ResponseEntity.status(200).build();
    }

    @GetMapping(value = "/obter-banner/{id}")
    public ResponseEntity<String> getBanner(@PathVariable @Valid int id) {

        String foto = empresaService.getBanner(id);

        return ResponseEntity.status(200).body(foto);
    }

    @Hidden
    @PutMapping("/atualizar")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<EmpresaListagemDto> atualizarEmpresa(@RequestBody @Valid EmpresaCriacaoDto empresaDto){
        Empresa empresa = empresaService.buscarPorCnpj(empresaDto.getCnpj());

        if (empresa == null){
            return ResponseEntity.status(404).build();
        }

        EmpresaListagemDto empresaSalva = empresaService.atualizarEmpresa(empresaDto, empresaDto.getCnpj());

        return ResponseEntity.status(200).body(empresaSalva);
    }
}
