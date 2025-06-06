package application.usecase

import domain.service.JwtService
import domain.service.RedisService
import presentation.dto.AuthTokens

class LoginByCodeUseCase(
    private val redisService: RedisService,
    private val jwtService: JwtService
) {
    operator fun invoke(email: String): AuthTokens {
        val confirmKey = "email:confirm:$email"
        val requestCountKey = "email:request-count:$email"
        val lastRequestKey = "email:last-request:$email"
        val verifyAttemptsKey = "email:verify-attempts:$email"

        val accessToken = jwtService.generateAccessToken(email)
        val refreshToken = jwtService.generateRefreshToken(email)

        redisService.del(confirmKey)
        redisService.del(requestCountKey)
        redisService.del(lastRequestKey)
        redisService.del(verifyAttemptsKey)

        return AuthTokens(accessToken, refreshToken)
    }
}