package presentation.http

import application.service.LoginByCodeService
import application.service.LoginByCodeVerifyService
import application.service.LoginByPasswordService
import application.service.RefreshTokenService
import application.service.RegisterByCodeService
import application.service.RegisterByCodeVerifyService
import application.usecase.RegisterConfirmUseCase
import application.service.LogoutService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    registerByCodeService: RegisterByCodeService,
    registerByCodeVerifyService: RegisterByCodeVerifyService,
    registerConfirmUseCase: RegisterConfirmUseCase,
    loginByCodeService: LoginByCodeService,
    loginByCodeVerifyService: LoginByCodeVerifyService,
    loginByPasswordService: LoginByPasswordService,
    refreshTokenService: RefreshTokenService,
    logoutService: LogoutService,
) {
    routing {
        route("/auth") {
            route("/register") {
                registerRoutes(
                    registerByCodeService = registerByCodeService,
                    registerByCodeVerifyService = registerByCodeVerifyService,
                    registerConfirmUseCase = registerConfirmUseCase
                )
            }

            route("/login") {
                loginRoutes(
                    loginByCodeService = loginByCodeService,
                    loginByCodeVerifyService = loginByCodeVerifyService,
                    loginByPasswordService = loginByPasswordService
                )
            }

            route("/refresh") {
                refreshTokenRoutes(
                    refreshTokenService = refreshTokenService
                )
            }

            route("/logout") {
                logoutRoutes(
                    logoutService = logoutService
                )
            }
        }
    }
}
