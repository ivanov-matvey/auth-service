package application.usecase

import domain.service.JwtService
import domain.service.RedisService
import presentation.dto.AuthTokensDTO
import shared.InvalidRefreshTokenException

class RefreshTokenUseCase(
    private val redisService: RedisService,
    private val jwtService: JwtService,
) {
    operator fun invoke(currentRefreshToken: String?): AuthTokensDTO {
        val token = currentRefreshToken?.takeIf { it.isNotBlank() }
            ?: throw InvalidRefreshTokenException()

        val exists = redisService.get("refresh:$token") != null
        if (!exists) throw InvalidRefreshTokenException()

        val email = jwtService.validateToken(token)
            ?: throw InvalidRefreshTokenException()

        val newAccessToken = jwtService.generateAccessToken(email)
        val newRefreshToken = jwtService.generateRefreshToken(email)

        val ttlRefreshTokenSeconds = System.getenv("TTL_REFRESH_TOKEN").toLong()

        redisService.setex("refresh:$newRefreshToken", "1", ttlRefreshTokenSeconds)
        redisService.del("refresh:$token")

        return AuthTokensDTO(newAccessToken, newRefreshToken)
    }
}
