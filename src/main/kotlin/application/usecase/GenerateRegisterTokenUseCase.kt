package application.usecase

import domain.service.RedisService
import presentation.dto.RegisterTokenDTO
import java.util.UUID

class GenerateRegisterTokenUseCase(
    private val redisService: RedisService
) {
    operator fun invoke(email: String): RegisterTokenDTO {
        val confirmKey = "email:confirm:$email"
        val requestCountKey = "email:request-count:$email"
        val lastRequestKey = "email:last-request:$email"
        val verifyAttemptsKey = "email:verify-attempts:$email"

        val token = UUID.randomUUID()
        redisService.setex("register:token:$token", email, 600)

        redisService.del(confirmKey)
        redisService.del(requestCountKey)
        redisService.del(lastRequestKey)
        redisService.del(verifyAttemptsKey)

        return RegisterTokenDTO(token)
    }
}