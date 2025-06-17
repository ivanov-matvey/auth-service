package infrastructure.config

import application.service.LoginByCodeService
import application.service.LoginByCodeVerifyService
import application.service.LoginByPasswordService
import application.service.RefreshTokenService
import application.service.RegisterByCodeService
import application.service.RegisterByCodeVerifyService
import application.usecase.CheckUserUseCase
import application.usecase.GenerateAccessTokenUseCase
import application.usecase.GenerateLoginTokensUseCase
import application.usecase.GenerateRegisterTokenUseCase
import application.usecase.LoginByPasswordUseCase
import application.usecase.RegisterConfirmUseCase
import application.usecase.SendCodeUseCase
import application.usecase.ValidateTokenUseCase
import application.usecase.VerifyCodeUseCase
import domain.service.EmailValidationService
import infrastructure.persistence.repository.PostgresUserRepositoryImpl
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
    val userRepository = PostgresUserRepositoryImpl()
    val emailValidationService = EmailValidationService()
    val mailService = MailServiceImpl
    val jwtService = JwtServiceImpl

    val registerByCodeService = RegisterByCodeService(
        checkUserUseCase = CheckUserUseCase(
            userRepository = userRepository,
            emailValidationService = emailValidationService,
        ),
        sendCodeUseCase = SendCodeUseCase(
            redisService = redisService,
            mailService = mailService,
        ),
    )

    val registerByCodeVerifyService = RegisterByCodeVerifyService(
        checkUserUseCase = CheckUserUseCase(
            userRepository = userRepository,
            emailValidationService = emailValidationService,
        ),
        verifyCodeUseCase = VerifyCodeUseCase(
            redisService = redisService
        ),
        generateRegisterTokenUseCase = GenerateRegisterTokenUseCase(
            redisService = redisService
        )
    )

    val registerConfirmUseCase = RegisterConfirmUseCase(
        redisService = redisService,
        userRepository = userRepository
    )

    val loginByCodeService = LoginByCodeService(
        checkUserUseCase = CheckUserUseCase(
            userRepository = userRepository,
            emailValidationService = emailValidationService
        ),
        sendCodeUseCase = SendCodeUseCase(
            redisService = redisService,
            mailService = mailService
        )
    )

    val loginByCodeVerifyService = LoginByCodeVerifyService(
        checkUserUseCase = CheckUserUseCase(
            userRepository = userRepository,
            emailValidationService = emailValidationService,
        ),
        verifyCodeUseCase = VerifyCodeUseCase(
            redisService = redisService
        ),
        generateLoginTokensUseCase = GenerateLoginTokensUseCase(
            redisService = redisService,
            jwtService = jwtService
        )
    )

    val loginByPasswordService = LoginByPasswordService(
        checkUserUseCase = CheckUserUseCase(
            userRepository = userRepository,
            emailValidationService = emailValidationService,
        ),
        loginByPasswordUseCase = LoginByPasswordUseCase(
            userRepository = userRepository,
            redisService = redisService,
            generateLoginTokensUseCase = GenerateLoginTokensUseCase(
                redisService = redisService,
                jwtService = jwtService
            )
        )
    )

    val refreshTokenService = RefreshTokenService(
        validateTokenUseCase = ValidateTokenUseCase(
            jwtService = jwtService
        ),
        generateAccessTokenUseCase = GenerateAccessTokenUseCase(
            jwtService = jwtService
        )
    )

    configureRouting(
        registerByCodeService = registerByCodeService,
        registerByCodeVerifyService = registerByCodeVerifyService,
        registerConfirmUseCase = registerConfirmUseCase,

        loginByCodeService = loginByCodeService,
        loginByCodeVerifyService = loginByCodeVerifyService,
        loginByPasswordService = loginByPasswordService,

        refreshTokenService = refreshTokenService
    )
}
