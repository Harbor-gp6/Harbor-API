package gp6.harbor.harborapi.dto.usuarioPrestador.autenticacao;

import gp6.harbor.harborapi.domain.prestador.repository.PrestadorRepository;
import gp6.harbor.harborapi.dto.usuarioPrestador.autenticacao.dto.UsuarioPrestadorDetalhesDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import gp6.harbor.harborapi.domain.prestador.Prestador;

import java.util.Optional;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private PrestadorRepository prestadorRepository;

    // Método da interface implementada
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Prestador> usuarioOpt = prestadorRepository.findByEmail(username);

        if (usuarioOpt.isEmpty()) {

            throw new UsernameNotFoundException(String.format("usuario: %s nao encontrado", username));
        }

        return new UsuarioPrestadorDetalhesDto(usuarioOpt.get());
    }
}
