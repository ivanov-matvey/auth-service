package infrastructure.service

import domain.service.MailService
import jakarta.mail.*
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import java.util.Properties

object MailServiceImpl : MailService {
    private val username = System.getenv("MAIL_USER")
    private val password = System.getenv("MAIL_PASSWORD")

    private val props = Properties().apply {
        put("mail.smtp.auth", "true")
        put("mail.smtp.starttls.enable", "true")
        put("mail.smtp.host", "smtp.gmail.com")
        put("mail.smtp.port", "587")
    }

    private val session: Session = Session.getInstance(props, object : Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication(username, password)
        }
    })

    private val templateEngine: TemplateEngine by lazy {
        val resolver = ClassLoaderTemplateResolver().apply {
            prefix = "templates/"
            suffix = ".html"
            characterEncoding = "UTF-8"
            templateMode = TemplateMode.HTML
        }
        TemplateEngine().apply {
            setTemplateResolver(resolver)
        }
    }

    override fun sendVerificationEmail(to: String, code: String) {
        val context = Context().apply {
            setVariable("code", code)
        }
        val htmlContent = templateEngine.process("email-template", context)

        val message = MimeMessage(session).apply {
            setFrom(InternetAddress(username))
            setRecipients(Message.RecipientType.TO, InternetAddress.parse(to))
            subject = "Код для подтверждения регистрации"
            setContent(htmlContent, "text/html; charset=utf-8")
        }

        Transport.send(message)
    }
}
