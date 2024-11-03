package gp6.harbor.harborapi.service.empresa;

import gp6.harbor.harborapi.api.enums.CargoEnum;
import gp6.harbor.harborapi.domain.cliente.Cliente;
import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.empresa.repository.EmpresaRepository;
import gp6.harbor.harborapi.domain.endereco.Endereco;
import gp6.harbor.harborapi.domain.endereco.repository.EnderecoRepository;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import gp6.harbor.harborapi.domain.prestador.repository.PrestadorRepository;
import gp6.harbor.harborapi.dto.empresa.dto.EmpresaCriacaoDto;
import gp6.harbor.harborapi.dto.empresa.dto.EmpresaListagemDto;
import gp6.harbor.harborapi.dto.empresa.dto.EmpresaMapperStruct;
import gp6.harbor.harborapi.dto.endereco.dto.EnderecoCriacaoDto;
import gp6.harbor.harborapi.exception.ConflitoException;
import gp6.harbor.harborapi.exception.NaoEncontradoException;
import gp6.harbor.harborapi.service.endereco.EnderecoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmpresaService {

    private final PrestadorRepository prestadorRepository;
    private final EmpresaRepository empresaRepository;
    private final EnderecoRepository enderecoRepository;
    private final EnderecoService enderecoService;
    private final EmpresaMapperStruct empresaMapperStruct;

    public Empresa cadastrar(Empresa empresa) {
        if (existePorCnpj(empresa.getCnpj())) {
            throw new ConflitoException("Empresa");
        }

        if (empresa.getHorarioAbertura().isAfter(empresa.getHorarioFechamento())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return empresaRepository.save(empresa);
    }
    public Empresa cadastrar(EmpresaCriacaoDto empresa) {
        if (existePorCnpj(empresa.getCnpj())) {
            throw new ConflitoException("Empresa");
        }

        if (empresa.getHorarioAbertura().isAfter(empresa.getHorarioFechamento())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        Empresa empresaCriacao = empresaMapperStruct.toEntity(empresa);

        return empresaRepository.save(empresaCriacao);
    }

    public List<Empresa> listar() { return empresaRepository.findAll(); }

    public Empresa buscarPorId(int id) {
        return empresaRepository.findById(id).orElseThrow(() -> new NaoEncontradoException("Empresa"));
    }

    public Empresa buscarPorCnpj(String cnpj) {
        return empresaRepository.findByCnpj(cnpj).orElseThrow(() -> new NaoEncontradoException("Empresa"));
    }

    public boolean existePorCnpj(String cnpj) {
        return empresaRepository.existsByCnpj(cnpj);
    }

    public boolean existePorId(Integer id) {
        return empresaRepository.existsById(id);
    }

    public void inativarEmpresa(int id) {
        Empresa empresa = buscarPorId(id);
        empresa.setDataInativacao(LocalDate.now());
        empresaRepository.save(empresa);
    }

    public EmpresaListagemDto atualizarEmpresa(EmpresaCriacaoDto empresaDto, String cnpj) {
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Prestador prestadorLogado = prestadorRepository.findByEmail(emailUsuario).orElse(null);

        if (prestadorLogado == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "O usuário precisa estar logado");
        }

        if (prestadorLogado.getCargo() != CargoEnum.ADMIN) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Apenas administradores podem atualizar empresas");
        }

        if (!prestadorLogado.getEmpresa().getCnpj().equals(cnpj)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Apenas é permitido atualizar a própria empresa");
        }

        Empresa empresa = prestadorLogado.getEmpresa();

        if (empresa.getHorarioAbertura().isAfter(empresa.getHorarioFechamento())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        //buscar endereco da empresa e atribuir o novo endereco
        Endereco endereco = enderecoService.buscarPorId(empresa.getEndereco().getId());

        EnderecoCriacaoDto enderecoAtualizado = empresaDto.getEndereco();
        endereco.setCep(enderecoAtualizado.getCep());
        endereco.setLogradouro(enderecoAtualizado.getLogradouro());
        endereco.setNumero(enderecoAtualizado.getNumero());
        endereco.setComplemento(enderecoAtualizado.getComplemento());
        endereco.setBairro(enderecoAtualizado.getBairro());
        endereco.setCidade(enderecoAtualizado.getCidade());
        endereco.setEstado(enderecoAtualizado.getEstado());

        empresa.setEndereco(endereco);
        empresa.setRazaoSocial(empresaDto.getRazaoSocial());
        empresa.setNomeFantasia(empresaDto.getNomeFantasia());
        empresa.setCnpj(empresaDto.getCnpj());
        empresa.setHorarioAbertura(empresaDto.getHorarioAbertura());
        empresa.setHorarioFechamento(empresaDto.getHorarioFechamento());

        if (validar(empresa) && enderecoService.validarEndereco(endereco)) {
            empresaRepository.save(empresa);
            enderecoRepository.save(endereco);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados inválidos, verifique os campos e tente novamente.");
        }

        return empresaMapperStruct.toDtoListagem(empresa);
    }

    public boolean validar(Empresa empresa){
        return empresa.getRazaoSocial() != null && empresa.getCnpj() != null && empresa.getHorarioAbertura() != null && empresa.getHorarioFechamento() != null && empresa.getEndereco() != null;
    }

    @Transactional
    public void setFoto(String novaFoto) {
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();

        Prestador prestadorLogado = prestadorRepository.findByEmail(emailUsuario).orElse(null);

        if (prestadorLogado == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "O usuário precisa estar logado");
        }

        Empresa empresa = prestadorLogado.getEmpresa();

        empresaRepository.setFoto(empresa.getId(), novaFoto);
    }

    @Transactional
    public void setBanner(String novaFoto) {
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();

        Prestador prestadorLogado = prestadorRepository.findByEmail(emailUsuario).orElse(null);

        if (prestadorLogado == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "O usuário precisa estar logado");
        }
        Empresa empresa = prestadorLogado.getEmpresa();

        empresaRepository.setBanner(empresa.getId(), novaFoto);
    }

    public String getBanner() {
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();

        Prestador prestadorLogado = prestadorRepository.findByEmail(emailUsuario).orElse(null);

        if (prestadorLogado == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "O usuário precisa estar logado");
        }
        Empresa empresa = prestadorLogado.getEmpresa();

        return empresaRepository.getBanner(empresa.getId());
    }

    public String getFoto() {
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();

        Prestador prestadorLogado = prestadorRepository.findByEmail(emailUsuario).orElse(null);

        if (prestadorLogado == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "O usuário precisa estar logado");
        }
        Empresa empresa = prestadorLogado.getEmpresa();

        return empresaRepository.getFoto(empresa.getId());
    }
}
