package presentation.http

import application.usecase.RegisterUseCase
import io.ktor.server.request.receive
import io.ktor.server.routing.*
import presentation.dto.RegisterRequest

fun Route.authRoutes(
    registerUseCase: RegisterUseCase,
) {
    post("/register") {
        val request = call.receive<RegisterRequest>()

        val result = registerUseCase.invoke(request.email)
    }

    post("/verify") {
        TODO("Not yet implemented")
    }

    post("/confirm-registration") {
        TODO("Not yet implemented")
    }
}