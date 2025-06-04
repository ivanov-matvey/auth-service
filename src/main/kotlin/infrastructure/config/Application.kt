package infrastructure.config

import application.usecase.RegisterUseCase
import domain.service.EmailValidationService
import infrastructure.persistence.repository.PostgresUserRepository
import infrastructure.service.RedisService
import presentation.http.configureRouting
import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    configureStatusPages()
    configureSerialization()
    configureDatabase()

    val registerUseCase = RegisterUseCase(
        userRepository = PostgresUserRepository(),
        emailValidationService = EmailValidationService(),
        redisService = RedisService(RedisProvider.commands)
    )

    configureRouting(registerUseCase)
}
