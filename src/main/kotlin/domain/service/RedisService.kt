package domain.service

interface RedisService {
    fun setex(key: String, value: String, ttlSeconds: Long): String?
    fun get(key: String): String?
    fun del(key: String): Long
    fun ttl(key: String): Long
}
