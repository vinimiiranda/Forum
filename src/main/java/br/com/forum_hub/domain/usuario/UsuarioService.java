package br.com.forum_hub.domain.usuario;

import br.com.forum_hub.domain.perfil.DadosPerfil;
import br.com.forum_hub.domain.perfil.PerfilNome;
import br.com.forum_hub.domain.perfil.PerfilRepository;
import br.com.forum_hub.infra.email.EmailService;
import br.com.forum_hub.infra.exception.RegraDeNegocioException;
import br.com.forum_hub.infra.security.HierarquiaService;
import br.com.forum_hub.infra.security.totp.TotpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final PerfilRepository perfilRepository;
    private final HierarquiaService hierarquiaService;
    private final TotpService totpService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmailIgnoreCaseAndVerificadoTrueAndAtivoTrue(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuário não encontrado, não verificado ou inativo"
                ));
    }

    @Transactional
    public Usuario cadastrar(DadosCadastroUsuario dados) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findByEmailIgnoreCaseOrNomeUsuarioIgnoreCase(dados.email(), dados.nomeUsuario());

        if (optionalUsuario.isPresent()) {
            throw new RegraDeNegocioException("Já existe uma conta cadastrada com esse email ou nome de usuário!");
        }

        var senhaCriptografada = passwordEncoder.encode(dados.senha());
        var perfil = perfilRepository.findByNome(PerfilNome.ESTUDANTE);
        var usuario = new Usuario(dados, senhaCriptografada, perfil, false);

        emailService.enviarEmailVerificacao(usuario);
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void verificarEmail(String codigo) {
        var usuario = usuarioRepository.findByToken(codigo).orElseThrow();
        usuario.verificar();
    }

    public Usuario buscarPeloNomeUsuario(String nomeUsuario) {
        return usuarioRepository.findByNomeUsuarioIgnoreCaseAndVerificadoTrueAndAtivoTrue(nomeUsuario)
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado!"));
    }

    @Transactional
    public Usuario editarPerfil(Usuario usuario, @Valid DadosEdicaoUsuario dados) {
        return usuario.alterarDados(dados);
    }

    @Transactional
    public void alterarSenha(@Valid DadosAlteracaoSenha dados, Usuario logado) {
        if (!passwordEncoder.matches(dados.senhaAtual(), logado.getPassword())) {
            throw new RegraDeNegocioException("Senha digitada não confere com senha atual!");
        }
        if (!dados.novaSenha().equals(dados.novaSenhaConfirmacao())) {
            throw new RegraDeNegocioException("Senha e confirmação não conferem!");
        }

        String senhaCriptografada = passwordEncoder.encode(dados.novaSenha());
        logado.alterarSenha(senhaCriptografada);
    }

    @Transactional
    public void desativarUsuario(Long id, Usuario usuario) {
        usuario.desativar();
    }

    @Transactional
    public Usuario adicionarPerfil(Long id, @Valid DadosPerfil dados) {
        var usuario = usuarioRepository.findById(id).orElseThrow();
        var perfil = perfilRepository.findByNome(dados.perfilNome());
        usuario.adcionarPerfil(perfil);
        return usuario;
    }

    public Usuario removerPerfil(Long id, @Valid DadosPerfil dados) {
        var usuario = usuarioRepository.findById(id).orElseThrow();
        var perfil = perfilRepository.findByNome(dados.perfilNome());
        usuario.removerPerfil(perfil);
        return usuario;
    }

    @Transactional
    public void reativarUsuario(Long id) {
        var usuario = usuarioRepository.findById(id).orElseThrow();
        usuario.reativar();
    }

    @Transactional
    public Usuario cadastrarVerificado(DadosCadastroUsuario dados) {
        var usuario = criarUsuario(dados, true);
        return usuarioRepository.save(usuario);
    }

    private Usuario criarUsuario(DadosCadastroUsuario dados, Boolean verificado) {
        var senhaCriptografada = passwordEncoder.encode(dados.senha());
        var perfil = perfilRepository.findByNome(PerfilNome.ESTUDANTE);
        return new Usuario(dados, senhaCriptografada, perfil, verificado);
    }

    @Transactional
    public String gerarQrCode(Usuario logado) {
        var secret = totpService.gerarSecret();
        logado.gerarSecret(secret);
        usuarioRepository.save(logado);

        return totpService.gerarQrCode(logado);
    }

    @Transactional
    public void ativarA2f(String codigo, Usuario logado) {

        if (logado.isA2fAtiva()) {
            throw new RegraDeNegocioException("Sua autenticação de dois fatores já esta ativa!");
        }
        var codigoValido = totpService.verificarCodigo(codigo,logado);
        if (!codigoValido){
            throw new RegraDeNegocioException("Código enválido!");
        }
        logado.ativarA2f();
        usuarioRepository.save(logado);
    }
}