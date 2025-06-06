package application.util

import domain.service.RedisService
import kotlin.text.toIntOrNull

fun redisLimiter(
    redisService: RedisService,
    key: String,
    limit: Int,
    message: (Long) -> String,
    ttlSeconds: Long
) {
    val current = redisService.get(key)?.toIntOrNull() ?: 0
    if (current >= limit) {
        val ttl = redisService.ttl(key)
        throw IllegalArgumentException(message(ttl))
    }
    redisService.setex(key, (current + 1).toString(), ttlSeconds)

}
