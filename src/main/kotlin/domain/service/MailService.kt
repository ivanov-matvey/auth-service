package domain.service

interface MailService {
    fun sendVerificationEmail(to: String, code: String)
}
