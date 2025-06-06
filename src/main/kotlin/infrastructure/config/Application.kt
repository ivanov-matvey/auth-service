package infrastructure.config

import application.usecase.RegisterConfirmUseCase
import application.usecase.RegisterUseCase
import application.usecase.RegisterVerifyUseCase
import domain.service.EmailValidationService
import infrastructure.persistence.repository.PostgresUserRepository
import infrastructure.service.MailServiceImpl
import infrastructure.service.RedisServiceImpl
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
        redisService = RedisServiceImpl(RedisProvider.commands),
        emailValidationService = EmailValidationService(),
        mailService = MailServiceImpl
    )
    val registerVerifyUseCase = RegisterVerifyUseCase(
        redisService = RedisServiceImpl(RedisProvider.commands)
    )
    val registerConfirmUseCase = RegisterConfirmUseCase(
        userRepository = PostgresUserRepository(),
        redisService = RedisServiceImpl(RedisProvider.commands)
    )

    configureRouting(
        registerUseCase,
        registerVerifyUseCase,
        registerConfirmUseCase
    )
}
