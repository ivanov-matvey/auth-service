package infrastructure.service

import domain.service.RedisService
import io.lettuce.core.api.sync.RedisCommands

class RedisServiceImpl(private val commands: RedisCommands<String, String>) :
    RedisService {
    override fun setex(key: String, value: String, ttlSeconds: Long): String? = commands.setex(key, ttlSeconds, value)
    override fun get(key: String): String? = commands.get(key)
}
