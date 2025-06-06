package application.usecase

import application.util.redisLimiter
import domain.service.RedisService
import shared.TooManyAttemptsException

class CodeVerifyUseCase(
    private val redisService: RedisService
) {
    operator fun invoke(email: String, code: String): Boolean {
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
