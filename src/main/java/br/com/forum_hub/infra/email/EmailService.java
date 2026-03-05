package br.com.forum_hub.infra.email;
import br.com.forum_hub.domain.usuario.Usuario;
import br.com.forum_hub.infra.exception.RegraDeNegocioException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    private final JavaMailSender enviadorEmail;

    @Value("${spring.mail.username}")
    private String emailOrigem;

    private static final String NOME_ENVIADOR = "Forum Hub";
    public static final String URL_SITE = "http://localhost:8080";

    public EmailService(JavaMailSender enviadorEmail) {
        this.enviadorEmail = enviadorEmail;
    }

    public void enviarEmail(String emailUsuario, String assunto, String conteudo) {
        MimeMessage message = enviadorEmail.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(emailOrigem, NOME_ENVIADOR);
            helper.setTo(emailUsuario);
            helper.setSubject(assunto);
            helper.setText(conteudo, true);

            enviadorEmail.send(message);

        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RegraDeNegocioException("Erro ao enviar email");
        }
    }

    public void enviarEmailVerificacao(Usuario usuario) {
        String assunto = "Aqui está seu link para verificar o email";

        String conteudo = gerarConteudoEmail(
                "Olá [[name]],<br>"
                        + "Por favor clique no link abaixo para verificar sua conta:<br>"
                        + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFICAR</a></h3>"
                        + "Obrigado,<br>"
                        + "Fórum Hub :).",
                usuario.getNomeCompleto(),
                URL_SITE + "/verificar-conta?codigo=" + usuario.getToken()
        );

        enviarEmail(usuario.getUsername(), assunto, conteudo); // getUsername() = email
    }

    private String gerarConteudoEmail(String template, String nome, String url) {
        return template.replace("[[name]]", nome).replace("[[URL]]", url);
    }
}