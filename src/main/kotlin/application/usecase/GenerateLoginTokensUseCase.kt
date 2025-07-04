package application.usecase

import domain.service.JwtService
import domain.service.RedisService
import presentation.dto.AuthTokensDTO

class GenerateLoginTokensUseCase(
    private val redisService: RedisService,
    private val jwtService: JwtService
) {
    private val ttlRefreshTokenSeconds = System.getenv("TTL_REFRESH_TOKEN").toLong()

    operator fun invoke(email: String): AuthTokensDTO {
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

        redisService.setex("refresh:$refreshToken", "1", ttlRefreshTokenSeconds)

        return AuthTokensDTO(accessToken, refreshToken)
    }
}