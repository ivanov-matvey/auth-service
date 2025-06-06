package infrastructure.config

import application.service.RegisterService
import application.usecase.RegisterConfirmUseCase
import application.usecase.CodeSendUseCase
import application.usecase.CodeVerifyUseCase
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

    val redisService = RedisServiceImpl(RedisProvider.commands)
    val userRepository = PostgresUserRepository()
    val emailValidationService = EmailValidationService()

    val codeSendUseCase = CodeSendUseCase(
        userRepository = userRepository,
        redisService = redisService,
        emailValidationService = emailValidationService,
        mailService = MailServiceImpl
    )

    val codeVerifyUseCase = CodeVerifyUseCase(redisService)

    val registerVerifyUseCase = RegisterVerifyUseCase(redisService)

    val registerService = RegisterService(
        codeVerifyUseCase = codeVerifyUseCase,
        registerVerifyUseCase = registerVerifyUseCase
    )

    val registerConfirmUseCase = RegisterConfirmUseCase(
        userRepository = userRepository,
        redisService = redisService
    )

    configureRouting(
        codeSendUseCase,
        registerService,
        registerConfirmUseCase
    )
}
