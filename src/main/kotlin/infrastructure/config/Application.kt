package infrastructure.config

import application.usecase.RegisterUseCase
import infrastructure.persistence.repository.PostgresUserRepository
import presentation.http.configureRouting
import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    val userRepository = PostgresUserRepository()
    val registerUseCase = RegisterUseCase(userRepository)

    configureRouting(registerUseCase)
}
