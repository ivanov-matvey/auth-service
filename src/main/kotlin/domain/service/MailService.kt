package domain.service

interface MailService {
    fun sendVerificationEmail(to: String, code: String, type: CodeType)
}

enum class CodeType {
    REGISTER,
    LOGIN,
}
