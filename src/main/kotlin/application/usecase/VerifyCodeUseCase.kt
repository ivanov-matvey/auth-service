package application.usecase

import application.util.redisLimiter
import domain.service.RedisService
import domain.value.Email
import shared.TooManyAttemptsException

class VerifyCodeUseCase(
    private val redisService: RedisService
) {
    operator fun invoke(emailRaw: String, code: String): Boolean {
        val email = Email.create(emailRaw)

        val confirmKey = "email:confirm:$email"
        val verifyAttemptsKey = "email:verify-attempts:$email"

        redisLimiter(
            redisService = redisService,
            key = verifyAttemptsKey,
            limit = 5,
            exception = { ttl -> TooManyAttemptsException(ttl) },
            ttlSeconds = 600
        )

        val correctCode = redisService.get(confirmKey)

        return code == correctCode
    }
}
