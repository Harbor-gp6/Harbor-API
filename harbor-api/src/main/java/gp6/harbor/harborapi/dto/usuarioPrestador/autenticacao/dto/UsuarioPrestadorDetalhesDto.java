package gp6.harbor.harborapi.dto.usuarioPrestador.autenticacao.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import gp6.harbor.harborapi.domain.prestador.Prestador;


import java.util.Collection;

public class UsuarioPrestadorDetalhesDto implements UserDetails {

    private final String nome;

    private final String email;

    private final String senha;

    public UsuarioPrestadorDetalhesDto(Prestador usuarioPrestador) {
        this.nome = usuarioPrestador.getNome();
        this.email = usuarioPrestador.getEmail();
        this.senha = usuarioPrestador.getSenha();
    }

    public String getNome() {
        return nome;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
