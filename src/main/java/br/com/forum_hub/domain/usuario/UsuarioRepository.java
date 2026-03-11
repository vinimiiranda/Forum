package br.com.forum_hub.domain.usuario;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmailIgnoreCaseAndVerificadoTrue(String email);

    Optional<Usuario> findByRefreshToken(String refreshToken);

    Optional<Usuario> findByEmailIgnoreCaseOrNomeUsuarioIgnoreCase(String email, String nomeUsuario);

    Optional<Usuario> findByToken(String codigo);

    Optional<Usuario> findByNomeUsuarioIgnoreCaseAndVerificadoTrueAndAtivoTrue(String nomeUsuario);

    Optional<Usuario> findByEmailIgnoreCase(String email);

    Optional<Usuario> findByEmailIgnoreCaseAndVerificadoTrueAndAtivoTrue(String email);
}
