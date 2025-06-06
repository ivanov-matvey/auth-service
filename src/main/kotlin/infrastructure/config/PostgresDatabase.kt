package infrastructure.config

import io.ktor.server.application.Application
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabase() {
    val user = System.getenv("POSTGRES_USER")
    val password = System.getenv("POSTGRES_PASSWORD")
    val host = System.getenv("POSTGRES_USERS_HOST")
    val db = System.getenv("POSTGRES_USERS_DB")

    Database.connect(
        "jdbc:postgresql://$host/$db",
        user = user,
        password = password
    )
}
