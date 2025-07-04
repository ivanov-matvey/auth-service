package application.usecase

import domain.service.JwtService
import domain.service.RedisService
import shared.InvalidAccessTokenException
import shared.InvalidRefreshTokenException
import java.time.Instant

class LogoutUseCase(
    private val redisService: RedisService,
    private val jwtService: JwtService,
) {
    operator fun invoke(accessToken: String?, refreshToken: String?) {
        val at = accessToken?.takeIf { it.isNotBlank() } ?: throw InvalidAccessTokenException()
        val rt = refreshToken?.takeIf { it.isNotBlank() } ?: throw InvalidRefreshTokenException()

        val parsed = jwtService.parseToken(at)
        val expiration = parsed.expiration.toInstant().epochSecond
        val now = Instant.now().epochSecond
        val ttl = (expiration - now).coerceAtLeast(0)

        redisService.setex("blacklist:$at", "1", ttl)
        redisService.del("refresh:$rt")
    }
}
