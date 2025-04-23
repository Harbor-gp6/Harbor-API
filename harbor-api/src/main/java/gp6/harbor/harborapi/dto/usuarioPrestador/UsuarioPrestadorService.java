package gp6.harbor.harborapi.dto.usuarioPrestador;

import gp6.harbor.harborapi.api.enums.CargoEnum;
import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import gp6.harbor.harborapi.domain.prestador.repository.PrestadorRepository;
import gp6.harbor.harborapi.dto.prestador.dto.*;
import gp6.harbor.harborapi.dto.usuarioPrestador.autenticacao.dto.UsuarioPrestadorLoginDto;
import gp6.harbor.harborapi.dto.usuarioPrestador.autenticacao.dto.UsuarioPrestadorTokenDto;
import gp6.harbor.harborapi.dto.usuarioPrestador.dto.UsuarioPrestadorMapper;
import gp6.harbor.harborapi.exception.ConflitoException;
import gp6.harbor.harborapi.service.empresa.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import gp6.harbor.harborapi.api.configuration.security.jwt.GerenciadorTokenJwt;

@Service
public class UsuarioPrestadorService {

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private GerenciadorTokenJwt gerenciadorTokenJwt;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PrestadorRepository prestadorRepository;

    @Autowired
    private PrestadorMapperStruct prestadorMapperStruct;

    public void criar(PrestadorCriacaoDto usuarioPrestadorCriacaoDto) {
        final Prestador novoUsuario = PrestadorMapper.toEntity(usuarioPrestadorCriacaoDto);
        novoUsuario.setCargo(CargoEnum.valueOf("ADMIN"));

        if (usuarioPrestadorCriacaoDto.getEmpresaId() != null) {
            Empresa empresa = empresaService.buscarPorId(usuarioPrestadorCriacaoDto.getEmpresaId());
            if (empresa == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada");
            }
            novoUsuario.setEmpresa(empresa);
        } else {
            if (empresaService.existePorCnpj(usuarioPrestadorCriacaoDto.getEmpresa().getCnpj())) {
                novoUsuario.setEmpresa(empresaService.buscarPorCnpj(usuarioPrestadorCriacaoDto.getEmpresa().getCnpj()));
            } else {
                Empresa empresa = empresaService.cadastrar(usuarioPrestadorCriacaoDto.getEmpresa());
                novoUsuario.setEmpresa(empresa);
            }
        }

        String senhaCriptografada = passwordEncoder.encode(novoUsuario.getSenha());
        novoUsuario.setSenha(senhaCriptografada);

        if (validar(novoUsuario)) {
            this.prestadorRepository.save(novoUsuario);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Dados inválidos, verifique os campos e tente novamente.");
        }
    }

    public PrestadorFuncionarioCriacao atualizarFuncionario(PrestadorFuncionarioCriacao prestadorDto, String cpf) {
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Prestador prestadorLogado = prestadorRepository.findByEmail(emailUsuario).orElse(null);
        if (prestadorLogado == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "O usuário precisa estar logado");
        }
        Empresa empresa = prestadorLogado.getEmpresa();

        Prestador prestador = prestadorRepository.findByCpf(cpf);

        if (!prestador.getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName())
                && !"ADMIN".equals(prestador.getCargo())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Usuário não tem permissão para atualizar funcionários");
        }

        prestador.setNome(prestadorDto.getNome());
        prestador.setSobrenome(prestadorDto.getSobrenome());
        prestador.setTelefone(prestadorDto.getTelefone());
        prestador.setCpf(prestadorDto.getCpf());
        prestador.setEmail(prestadorDto.getEmail());
        prestador.setSenha(passwordEncoder.encode(prestadorDto.getSenha()));
        prestador.setCargo(prestadorDto.getCargo());

        if (validar(prestador)) {
            prestadorRepository.save(prestador);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Dados inválidos, verifique os campos e tente novamente.");
        }

        return prestadorMapperStruct.toDto(prestador);

    }

    public PrestadorFuncionarioCriacao atualizarFuncionarioSemSenha(PrestadorEdicaoDTO prestadorDto, String cpf) {
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Prestador prestadorLogado = prestadorRepository.findByEmail(emailUsuario).orElse(null);
        if (prestadorLogado == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "O usuário precisa estar logado");
        }
        Empresa empresa = prestadorLogado.getEmpresa();

        Prestador prestador = prestadorRepository.findByCpf(cpf);

        if (!prestador.getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName())
                && !"ADMIN".equals(prestador.getCargo())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Usuário não tem permissão para atualizar funcionários");
        }

        prestador.setNome(prestadorDto.getNome());
        prestador.setSobrenome(prestadorDto.getSobrenome());
        prestador.setTelefone(prestadorDto.getTelefone());
        prestador.setCpf(prestadorDto.getCpf());
        prestador.setEmail(prestadorDto.getEmail());
        // prestador.setSenha(passwordEncoder.encode(prestadorDto.getSenha()));
        // prestador.setCargo(prestadorDto.getCargo());

        if (validar(prestador)) {
            prestadorRepository.save(prestador);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Dados inválidos, verifique os campos e tente novamente.");
        }

        return prestadorMapperStruct.toDto(prestador);

    }

    public void criarFuncionario(PrestadorFuncionarioCriacao novoPrestadorDto) {
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Prestador prestador = prestadorRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Você precisa estar logado para criar funcionários"));

        if (prestador.getCargo().name() != "ADMIN") {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Usuário não tem permissão para criar funcionários");
        }

        Empresa empresa = prestador.getEmpresa();
        if (empresa == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada");
        }

        Prestador novoPrestador = prestadorMapperStruct.toEntity(novoPrestadorDto);
        novoPrestador.setEmpresa(empresa);
        novoPrestador.setSenha(passwordEncoder.encode(novoPrestador.getSenha()));

        if (validar(novoPrestador)) {
            prestadorRepository.save(novoPrestador);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Dados inválidos, verifique os campos e tente novamente.");
        }
    }

    public UsuarioPrestadorTokenDto autenticar(UsuarioPrestadorLoginDto usuarioPrestadorLoginDto) {

        final UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(
                usuarioPrestadorLoginDto.getEmail(), usuarioPrestadorLoginDto.getSenha());

        final Authentication authentication = this.authenticationManager.authenticate(credentials);

        Prestador usuarioPrestadorAutenticado = prestadorRepository.findByEmail(usuarioPrestadorLoginDto.getEmail())
                .orElseThrow(
                        () -> new ResponseStatusException(404, "Email do usuário não cadastrado", null));

        usuarioPrestadorAutenticado.validar();

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String token = gerenciadorTokenJwt.generateToken(authentication);

        return UsuarioPrestadorMapper.of(usuarioPrestadorAutenticado, token);
    }

    public boolean validar(Prestador prestador) {
        return prestador != null && prestador.getNome() != null && prestador.getSobrenome() != null
                && prestador.getTelefone() != null && prestador.getCpf() != null && prestador.getEmail() != null
                && prestador.getSenha() != null && prestador.getCargo() != null;
    }
}
