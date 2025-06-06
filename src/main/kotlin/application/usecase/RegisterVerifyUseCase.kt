package application.usecase

import application.util.redisLimiter
import domain.service.RedisService
import shared.InvalidConfirmationCodeException
import shared.TooManyAttemptsException
import java.util.UUID

class RegisterVerifyUseCase(
    private val redisService: RedisService
) {
    operator fun invoke(email: String, code: String): UUID {
        val confirmKey = "email:confirm:$email"
        val requestCountKey = "email:request-count:$email"
        val lastRequestKey = "email:last-request:$email"
        val verifyAttemptsKey = "email:verify-attempts:$email"

        redisLimiter(
            redisService = redisService,
            key = verifyAttemptsKey,
            limit = 5,
            exception = { ttl -> TooManyAttemptsException(ttl) },
            ttlSeconds = 600
        )

        val correctCode = redisService.get(confirmKey)
        if (code != correctCode) {
            throw InvalidConfirmationCodeException()
        }

        val token = UUID.randomUUID()
        redisService.setex("register:token:$token", email, 600)

        redisService.del(confirmKey)
        redisService.del(requestCountKey)
        redisService.del(lastRequestKey)
        redisService.del(verifyAttemptsKey)

        return token
    }
}