package application.usecase

import application.util.redisLimiter
import domain.service.CodeType
import domain.service.MailService
import domain.service.RedisService
import domain.value.Email
import shared.TooManyRequestsException

class SendCodeUseCase(
    private val redisService: RedisService,
    private val mailService: MailService
) {
    private val ttlVerificationCodeSeconds = System.getenv("TTL_VERIFICATION_CODE").toLong()

    operator fun invoke(emailRaw: String, type: CodeType = CodeType.REGISTER) {
        val email = Email.create(emailRaw)

        val confirmKey = "email:confirm:$email"
        val requestCountKey = "email:request-count:$email"

        val verificationCode = generateVerificationCode()
        redisService.setex(confirmKey, verificationCode, ttlVerificationCodeSeconds)

        redisLimiter(
            redisService = redisService,
            key = requestCountKey,
            limit = 5,
            exception = { ttl -> TooManyRequestsException(ttl) },
            ttlSeconds = 600
        )

        mailService.sendVerificationEmail(email.toString(), verificationCode, type)
    }

    private fun generateVerificationCode(): String {
        return (100000..999999).random().toString()
    }
}
