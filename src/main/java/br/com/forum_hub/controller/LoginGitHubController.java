package br.com.forum_hub.controller;

import br.com.forum_hub.domain.autenticacao.DadosToken;
import br.com.forum_hub.domain.autenticacao.TokenService;
import br.com.forum_hub.domain.autenticacao.github.LoginGitHubService;
import br.com.forum_hub.domain.usuario.Usuario;
import br.com.forum_hub.domain.usuario.UsuarioRepository;
import br.com.forum_hub.domain.usuario.UsuarioService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/login/github")
public class LoginGitHubController {

    private final LoginGitHubService loginGitHubService;
    private final UsuarioRepository usuarioRepository;
    private final TokenService tokenService;
    private final UsuarioService usuarioService;

    public LoginGitHubController(LoginGitHubService loginGitHubService, UsuarioRepository usuarioRepository, TokenService tokenService, UsuarioService usuarioService) {
        this.loginGitHubService = loginGitHubService;
        this.usuarioRepository = usuarioRepository;
        this.tokenService = tokenService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<Void> redirecionarGitHub() {
        var url = loginGitHubService.gerarUrl();
        var headers = new HttpHeaders();
        headers.setLocation(URI.create(url));

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }


    @GetMapping("/autorizado")
    public ResponseEntity<DadosToken> autenticarUsuarioOauth(@RequestParam String code) {
        var email = loginGitHubService.obterEmail(code);
        var usuario = (Usuario) usuarioService.loadUserByUsername(email);
        var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String tokenAcesso = tokenService.gerarToken((Usuario) authentication.getPrincipal());

        String refreshToken = tokenService.gerarRefreshToken((Usuario) authentication.getPrincipal());

        return ResponseEntity.ok(new DadosToken(tokenAcesso, refreshToken));

    }

    @GetMapping("/registro")
    public ResponseEntity<Void> redirecionarRegistroGithub() {
        var url = loginGitHubService.gerarUrl();
        var headers = new HttpHeaders();

        headers.setLocation(URI.create(url));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("/registro-autorizado")
    public ResponseEntity<DadosToken> registrarOAuth(@RequestParam String code) {
        var dadosUsuario = loginGitHubService.obterDadosOAuth(code);
        var usuario = usuarioService.cadastrarVerificado(dadosUsuario);
        var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String tokenAcesso = tokenService.gerarToken(usuario);
        String refreshToken = tokenService.gerarRefreshToken(usuario);

        return ResponseEntity.ok(new DadosToken(tokenAcesso, refreshToken));
    }
}
