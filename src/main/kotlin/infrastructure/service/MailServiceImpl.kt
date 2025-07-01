package infrastructure.service

import domain.service.CodeType
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
    private val smtpLogin = System.getenv("SMTP_LOGIN")
    private val smtpPassword = System.getenv("SMTP_PASSWORD")
    private val smtpHost = System.getenv("SMTP_HOST")
    private val smtpPort = System.getenv("SMTP_PORT")

    private val mailUser = System.getenv("MAIL_USER")

    private val props = Properties().apply {
        put("mail.smtp.auth", "true")
        put("mail.smtp.starttls.enable", "true")
        put("mail.smtp.host", smtpHost)
        put("mail.smtp.port", smtpPort)
    }

    private val session: Session = Session.getInstance(props, object : Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication(smtpLogin, smtpPassword)
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

    override fun sendVerificationEmail(to: String, code: String, type: CodeType) {
        val templateName = when (type) {
            CodeType.REGISTER -> "email-template-register"
            CodeType.LOGIN -> "email-template-login"
        }

        val subject = when (type) {
            CodeType.REGISTER -> "Код подтверждения регистрации"
            CodeType.LOGIN -> "Код подтверждения авторизации"
        }

        val context = Context().apply {
            setVariable("code", code)
        }

        val htmlContent = templateEngine.process(templateName, context)

        val message = MimeMessage(session).apply {
            setFrom(InternetAddress(mailUser))
            setRecipients(Message.RecipientType.TO, InternetAddress.parse(to))
            this.subject = subject
            setContent(htmlContent, "text/html; charset=utf-8")
        }

        Transport.send(message)
    }
}
