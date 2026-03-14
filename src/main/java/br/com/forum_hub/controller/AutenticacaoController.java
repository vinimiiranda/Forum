package br.com.forum_hub.controller;

import br.com.forum_hub.domain.autenticacao.*;
import br.com.forum_hub.domain.usuario.Usuario;
import br.com.forum_hub.domain.usuario.UsuarioRepository;
import br.com.forum_hub.infra.exception.RegraDeNegocioException;
import br.com.forum_hub.infra.security.totp.TotpService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class AutenticacaoController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;
    private final TotpService totpService;

    @PostMapping("/login")
    public ResponseEntity<DadosToken> efetuarLogin(@Valid @RequestBody DadosLogin dados) {
        var autenticationToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.password());
        var authetication = authenticationManager.authenticate(autenticationToken);
        var usuario = (Usuario) authetication.getPrincipal();
        if (usuario.isA2fAtiva()) {
            return ResponseEntity.ok(new DadosToken(null, null, true));
        }
        String tokenAcesso = tokenService.gerarToken(usuario);
        String refreshToken = tokenService.gerarRefreshToken(usuario);

        return ResponseEntity.ok(new DadosToken(tokenAcesso, refreshToken, false));
    }

    @PostMapping("/verificar-a2f")
    public ResponseEntity<DadosToken> verificarSegundoFator(@Valid @RequestBody DadosA2F dadosA2F) {
        var usuario = usuarioRepository.findByEmailIgnoreCaseAndVerificadoTrue(dadosA2F.email()).orElseThrow();
        var codigoValido = Boolean.parseBoolean(tokenService.verificarToken(dadosA2F.codigo()));
        if (!codigoValido) {
            throw new BadCredentialsException("Código enválido");
        }
        String tokenAcesso = tokenService.gerarToken(usuario);
        String refreshToken = tokenService.gerarRefreshToken(usuario);

        return ResponseEntity.ok(new DadosToken(tokenAcesso, refreshToken, false));
    }

    @PostMapping("/atualizar-token")
    public ResponseEntity<DadosToken> atualizarToken(@Valid @RequestBody DadosRefreshToken dados) {
        var refreshToken = dados.refreshToken();
        var usuario = usuarioRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RegraDeNegocioException("Refresh token inválido"));

        String tokenAcesso = tokenService.gerarToken(usuario);
        String novoRefreshToken = usuario.novoRefreshToken();

        return ResponseEntity.ok(new DadosToken(tokenAcesso, novoRefreshToken, false));
    }

}
