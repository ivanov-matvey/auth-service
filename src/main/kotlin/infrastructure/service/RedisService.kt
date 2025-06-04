package infrastructure.service

import io.lettuce.core.api.sync.RedisCommands

class RedisService(private val commands: RedisCommands<String, String>) {
    fun setex(key: String, value: String, ttlSeconds: Long) = commands.setex(key, ttlSeconds, value)
    fun get(key: String) = commands.get(key)
}
