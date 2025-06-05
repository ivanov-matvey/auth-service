package presentation.http

import application.usecase.RegisterConfirmUseCase
import application.usecase.RegisterUseCase
import application.usecase.RegisterVerifyUseCase
import io.ktor.server.application.*
import io.ktor.server.response.respondText
import io.ktor.server.routing.*

fun Application.configureRouting(
    registerUseCase: RegisterUseCase,
    registerVerifyUseCase: RegisterVerifyUseCase,
    registerConfirmUseCase: RegisterConfirmUseCase
) {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        route("/auth") {
            authRoutes(
                registerUseCase,
                registerVerifyUseCase,
                registerConfirmUseCase
            )
        }
    }
}
