package gp6.harbor.harborapi.dto.usuario;

import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import gp6.harbor.harborapi.domain.prestador.repository.PrestadorRepository;
import gp6.harbor.harborapi.dto.prestador.dto.PrestadorCriacaoDto;
import gp6.harbor.harborapi.dto.prestador.dto.PrestadorMapper;
import gp6.harbor.harborapi.service.empresa.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import gp6.harbor.harborapi.api.configuration.security.jwt.GerenciadorTokenJwt;
import gp6.harbor.harborapi.dto.usuario.autenticacao.dto.UsuarioLoginDto;
import gp6.harbor.harborapi.dto.usuario.autenticacao.dto.UsuarioTokenDto;
import gp6.harbor.harborapi.dto.usuario.dto.UsuarioMapper;


@Service
public class UsuarioService {

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PrestadorRepository usuarioRepository;

    @Autowired
    private GerenciadorTokenJwt gerenciadorTokenJwt;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PrestadorRepository prestadorRepository;

    public void criar(PrestadorCriacaoDto usuarioCriacaoDto) {
        final Prestador novoUsuario = PrestadorMapper.toEntity(usuarioCriacaoDto);
        if (usuarioCriacaoDto.getEmpresaId() != null) {
            Empresa empresa = empresaService.buscarPorId(usuarioCriacaoDto.getEmpresaId());
            novoUsuario.setEmpresa(empresa);
        }

        String senhaCriptografada = passwordEncoder.encode(novoUsuario.getSenha());
        novoUsuario.setSenha(senhaCriptografada);

        this.prestadorRepository.save(novoUsuario);
    }

    public void criarFuncionario(Prestador novoPrestador) {
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Prestador prestador = prestadorRepository.findByEmail(emailUsuario).orElse(null);
        Empresa empresa = prestador.getEmpresa();

        novoPrestador.setEmpresa(empresa);

        String senhaCriptografada = passwordEncoder.encode(novoPrestador.getSenha());
        novoPrestador.setSenha(senhaCriptografada);

        this.prestadorRepository.save(novoPrestador);
    }

    public UsuarioTokenDto autenticar(UsuarioLoginDto usuarioLoginDto) {

        final UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(
                usuarioLoginDto.getEmail(), usuarioLoginDto.getSenha());

        final Authentication authentication = this.authenticationManager.authenticate(credentials);

        Prestador usuarioAutenticado =
                usuarioRepository.findByEmail(usuarioLoginDto.getEmail())
                        .orElseThrow(
                                () -> new ResponseStatusException(404, "Email do usuário não cadastrado", null)
                        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String token = gerenciadorTokenJwt.generateToken(authentication);

        return UsuarioMapper.of(usuarioAutenticado, token);
    }
}