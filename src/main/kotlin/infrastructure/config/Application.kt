package infrastructure.config

import application.service.LoginService
import application.service.RegisterService
import application.usecase.RegisterConfirmUseCase
import application.usecase.CodeSendUseCase
import application.usecase.CodeVerifyUseCase
import application.usecase.LoginByCodeUseCase
import application.usecase.RegisterUseCase
import application.usecase.UserExistsUseCase
import domain.service.EmailValidationService
import infrastructure.persistence.repository.PostgresUserRepository
import infrastructure.service.JwtServiceImpl
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
    val mailService = MailServiceImpl
    val jwtService = JwtServiceImpl

    val codeSendUseCase = CodeSendUseCase(
        redisService = redisService,
        emailValidationService = emailValidationService,
        mailService = mailService
    )

    val codeVerifyUseCase = CodeVerifyUseCase(redisService)

    val registerVerifyUseCase = RegisterUseCase(redisService)

    val registerService = RegisterService(
        codeVerifyUseCase = codeVerifyUseCase,
        registerVerifyUseCase = registerVerifyUseCase
    )

    val registerConfirmUseCase = RegisterConfirmUseCase(
        userRepository = userRepository,
        redisService = redisService
    )

    val loginByCodeUseCase = LoginByCodeUseCase(
        redisService = redisService,
        jwtService = jwtService
    )

    val loginService = LoginService(
        codeVerifyUseCase = codeVerifyUseCase,
        loginByCodeUseCase = loginByCodeUseCase
    )

    val userExistsUseCase = UserExistsUseCase(
        userRepository = userRepository
    )

    configureRouting(
        codeSendUseCase = codeSendUseCase,
        registerService = registerService,
        registerConfirmUseCase = registerConfirmUseCase,
        loginService = loginService,
        userExistsUseCase = userExistsUseCase,
    )
}
