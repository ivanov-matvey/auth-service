package infrastructure.persistence.repository

import domain.repository.RefreshTokenRepository
import domain.service.RedisService

class RefreshTokenRepositoryImpl (
    private val redisService: RedisService
): RefreshTokenRepository {
    private val ttlRefreshToken = System.getenv("TTL_REFRESH_TOKEN")

    override fun findUserByToken(token: String): String? {
        return redisService.get("refresh:$token")
    }

    override fun save(token: String, email: String) {
        redisService.setex(
            "refresh:$token",
            email,
            ttlRefreshToken.toLong()
        )
    }
}