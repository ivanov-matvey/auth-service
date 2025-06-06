package presentation.http

import application.service.RegisterService
import application.usecase.RegisterConfirmUseCase
import application.usecase.CodeSendUseCase
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    codeSendUseCase: CodeSendUseCase,
    registerService: RegisterService,
    registerConfirmUseCase: RegisterConfirmUseCase
) {
    routing {
        route("/auth") {
            route("/register") {
                registerRoutes(
                    registerService,
                    registerConfirmUseCase
                )
            }

            route("/login") {
                loginRoutes()
            }

            route("/code") {
                codeRoutes(codeSendUseCase)
            }

            route("/refresh") {
                refreshTokenRoutes()
            }
        }
    }
}
