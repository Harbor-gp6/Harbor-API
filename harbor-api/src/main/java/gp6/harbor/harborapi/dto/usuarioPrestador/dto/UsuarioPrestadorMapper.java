package gp6.harbor.harborapi.dto.usuarioPrestador.dto;


import gp6.harbor.harborapi.domain.prestador.Prestador;
import gp6.harbor.harborapi.dto.usuarioPrestador.autenticacao.dto.UsuarioPrestadorTokenDto;

public class UsuarioPrestadorMapper {

    public static Prestador of(UsuarioPrestadorCriacaoDto usuarioPrestadorCriacaoDto) {
        Prestador usuarioPrestador = new Prestador();

        usuarioPrestador.setEmail(usuarioPrestadorCriacaoDto.getEmail());
        usuarioPrestador.setNome(usuarioPrestadorCriacaoDto.getNome());
        usuarioPrestador.setSenha(usuarioPrestadorCriacaoDto.getSenha());

        return usuarioPrestador;
    }

    public static UsuarioPrestadorTokenDto of(Prestador usuarioPrestador, String token) {
        UsuarioPrestadorTokenDto usuarioPrestadorTokenDto = new UsuarioPrestadorTokenDto();

        usuarioPrestadorTokenDto.setUserId(usuarioPrestador.getId());
        usuarioPrestadorTokenDto.setEmail(usuarioPrestador.getEmail());
        usuarioPrestadorTokenDto.setIdEmpresa(usuarioPrestador.getEmpresa().getId());
        usuarioPrestadorTokenDto.setNome(usuarioPrestador.getNome());
        usuarioPrestadorTokenDto.setToken(token);

        return usuarioPrestadorTokenDto;
    }
}
