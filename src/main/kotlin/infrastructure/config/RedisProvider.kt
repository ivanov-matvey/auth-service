package infrastructure.config

import io.lettuce.core.RedisClient
import io.lettuce.core.api.sync.RedisCommands

object RedisProvider {
    private val host = System.getenv("REDIS_HOST")
    private val port = System.getenv("REDIS_PORT")
    private val client = RedisClient.create("redis://$host:$port")
    private val connection = client.connect()

    val commands: RedisCommands<String, String> = connection.sync()
}
