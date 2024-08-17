package gp6.harbor.harborapi.dto.usuario.dto;


import gp6.harbor.harborapi.domain.prestador.Prestador;
import gp6.harbor.harborapi.dto.usuario.autenticacao.dto.UsuarioTokenDto;

public class UsuarioMapper {

    public static Prestador of(UsuarioCriacaoDto usuarioCriacaoDto) {
        Prestador usuario = new Prestador();

        usuario.setEmail(usuarioCriacaoDto.getEmail());
        usuario.setNome(usuarioCriacaoDto.getNome());
        usuario.setSenha(usuarioCriacaoDto.getSenha());

        return usuario;
    }

    public static UsuarioTokenDto of(Prestador usuario, String token) {
        UsuarioTokenDto usuarioTokenDto = new UsuarioTokenDto();

        usuarioTokenDto.setUserId(usuario.getId());
        usuarioTokenDto.setEmail(usuario.getEmail());
        usuarioTokenDto.setNome(usuario.getNome());
        usuarioTokenDto.setToken(token);

        return usuarioTokenDto;
    }
}