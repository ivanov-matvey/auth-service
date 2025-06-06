package presentation.http

import application.service.LoginService
import application.service.RegisterService
import application.usecase.RegisterConfirmUseCase
import application.usecase.CodeSendUseCase
import application.usecase.UserExistsUseCase
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    codeSendUseCase: CodeSendUseCase,
    registerService: RegisterService,
    registerConfirmUseCase: RegisterConfirmUseCase,
    loginService: LoginService,
    userExistsUseCase: UserExistsUseCase
) {
    routing {
        route("/auth") {
            route("/register") {
                registerRoutes(
                    registerService,
                    registerConfirmUseCase,
                    userExistsUseCase,
                    codeSendUseCase
                )
            }

            route("/login") {
                loginRoutes(
                    loginService,
                    codeSendUseCase
                )
            }

            route("/refresh") {
                refreshTokenRoutes()
            }
        }
    }
}
