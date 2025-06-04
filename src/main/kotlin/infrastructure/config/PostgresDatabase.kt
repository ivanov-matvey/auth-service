package infrastructure.config

import io.ktor.server.application.Application
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabase() {
    val postgresHost = System.getenv("POSTGRES_USERS_HOST")

    Database.connect(
        "jdbc:postgresql://$postgresHost/users",
        user = "postgres",
        password = "postgres"
    )
}
