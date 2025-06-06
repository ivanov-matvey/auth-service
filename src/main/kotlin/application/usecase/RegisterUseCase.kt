package application.usecase

import application.util.redisLimiter
import domain.repository.UserRepository
import domain.service.EmailValidationService
import domain.service.MailService
import domain.service.RedisService
import shared.InvalidEmailException
import shared.TooManyRequestsException
import shared.UserAlreadyExistsException

class RegisterUseCase(
    private val userRepository: UserRepository,
    private val redisService: RedisService,
    private val emailValidationService: EmailValidationService,
    private val mailService: MailService
) {
    operator fun invoke(email: String) {
        if (!emailValidationService.isValid(email)) {
            throw InvalidEmailException()
        }

        val user = userRepository.findByEmail(email)
        if (user != null) {
            throw UserAlreadyExistsException()
        }

        val confirmKey = "email:confirm:$email"
        val requestCountKey = "email:request-count:$email"

        val verificationCode = generateVerificationCode()
        redisService.setex(confirmKey, verificationCode, 600)

        redisLimiter(
            redisService = redisService,
            key = requestCountKey,
            limit = 5,
            exception = { ttl -> TooManyRequestsException(ttl) },
            ttlSeconds = 3600
        )

        mailService.sendVerificationEmail(email, verificationCode)
    }

    private fun generateVerificationCode(): String {
        return (100000..999999).random().toString()
    }
}
