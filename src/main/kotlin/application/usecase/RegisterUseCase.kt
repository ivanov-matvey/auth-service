package application.usecase

import domain.repository.UserRepository
import domain.service.EmailValidationService
import domain.service.MailService
import domain.service.RedisService

class RegisterUseCase(
    private val userRepository: UserRepository,
    private val redisService: RedisService,
    private val emailValidationService: EmailValidationService,
    private val mailService: MailService
) {
    operator fun invoke(email: String) {
        if (!emailValidationService.isValid(email)) {
            throw IllegalArgumentException("Invalid email")
        }

        val user = userRepository.findByEmail(email)
        if (user != null) {
            throw IllegalArgumentException("User already exists")
        }

        val verificationCode = generateVerificationCode()
        redisService.setex(email, verificationCode, 600)

        mailService.sendVerificationEmail(email, verificationCode)
    }

    private fun generateVerificationCode(): String {
        return (100000..999999).random().toString()
    }
}
