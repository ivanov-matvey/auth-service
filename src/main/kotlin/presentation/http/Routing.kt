package presentation.http

import application.usecase.RegisterUseCase
import io.ktor.server.application.*
import io.ktor.server.response.respondText
import io.ktor.server.routing.*

fun Application.configureRouting(
    registerUseCase: RegisterUseCase,
) {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        route("/auth") {
            authRoutes(registerUseCase)
        }
    }
}
