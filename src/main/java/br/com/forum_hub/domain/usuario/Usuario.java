package br.com.forum_hub.domain.usuario;

import br.com.forum_hub.domain.perfil.Perfil;
import br.com.forum_hub.infra.exception.RegraDeNegocioException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nomeCompleto;
    private String email;
    private String senha;
    private String nomeUsuario;
    private String biografia;
    private String miniBiografia;
    private String refreshToken;
    private LocalDateTime expiracaoRefreshToken;
    private Boolean verificado;
    private String token;
    private LocalDateTime expiracaoToken;
    private Boolean ativo;
    private String secret;
    private Boolean a2fAtiva;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuarios_perfis",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "perfil_id"))
    private List<Perfil> perfis = new ArrayList<>();

    public Usuario(DadosCadastroUsuario dados, String senhaCriptografada, Perfil perfil, Boolean verificado) {
        this.nomeCompleto = dados.nomeCompleto();
        this.email = dados.email();
        this.senha = senhaCriptografada;
        this.nomeUsuario = dados.nomeUsuario();
        this.biografia = dados.biografia();
        this.miniBiografia = dados.miniBiografia();
        if (verificado) {
            aprovarUsuario();
        } else {
            this.verificado = false;
            this.token = UUID.randomUUID().toString();
            this.expiracaoToken = LocalDateTime.now().plusMinutes(30);
            this.ativo = false;
        }
        this.perfis.add(perfil);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return perfis;
    }

    public boolean refreshTokenExpirado() {
        return expiracaoRefreshToken.isBefore(LocalDateTime.now());
    }

    public String novoRefreshToken() {
        this.refreshToken = UUID.randomUUID().toString();
        this.expiracaoRefreshToken = LocalDateTime.now().plusMinutes(120);
        return refreshToken;
    }

    @Override
    public boolean isEnabled() {
        return ativo;
    }

    public void desativar() {
        this.ativo = false;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public void verificar() {
        if (expiracaoToken.isBefore(LocalDateTime.now())) {
            throw new RegraDeNegocioException("Link de verificação expirou!");
        }
        aprovarUsuario();
    }

    public Usuario alterarDados(DadosEdicaoUsuario dados) {
        if (dados.nomeUsuario() != null) {
            this.nomeUsuario = dados.nomeUsuario();
        }
        if (dados.miniBiografia() != null) {
            this.miniBiografia = dados.miniBiografia();
        }
        if (dados.biografia() != null) {
            this.biografia = dados.biografia();
        }
        return this;
    }

    public void alterarSenha(String senhaCriptografada) {
        this.senha = senhaCriptografada;
    }

    public void adcionarPerfil(Perfil perfil) {
        this.perfis.add(perfil);
    }

    public void removerPerfil(Perfil perfil) {
        this.perfis.remove(perfil);
    }

    public void reativar() {
        this.ativo = true;
    }

    private void aprovarUsuario() {
        this.verificado = true;
        this.ativo = true;
        this.token = null;
        this.expiracaoToken = null;
    }

    public void gerarSecret(String secret) {
        this.secret = secret;
    }

    public boolean isA2fAtiva() {
        return this.a2fAtiva;
    }

    public void ativarA2f() {
        this.a2fAtiva = true;
    }
}