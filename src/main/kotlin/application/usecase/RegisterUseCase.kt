package application.usecase

import application.util.redisLimiter
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
            throw IllegalArgumentException("Неверный адрес электронной почты.")
        }

        val user = userRepository.findByEmail(email)
        if (user != null) {
            throw IllegalArgumentException("Пользователь с такой эл.почтой уже существует.")
        }

        val confirmKey = "email:confirm:$email"
        val lastRequestKey = "email:last-request:$email"
        val requestCountKey = "email:request-count:$email"

        val verificationCode = generateVerificationCode()
        redisService.setex(confirmKey, verificationCode, 600)

        val now = System.currentTimeMillis().toString()
        redisService.setex(lastRequestKey, now, 60)

        redisLimiter(
            redisService = redisService,
            key = requestCountKey,
            limit = 5,
            message = { ttl -> "Слишком много запросов. Повторите попытку через ${ttl/60} минут." },
            ttlSeconds = 3600
        )

        mailService.sendVerificationEmail(email, verificationCode)
    }

    private fun generateVerificationCode(): String {
        return (100000..999999).random().toString()
    }
}
