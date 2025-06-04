package presentation.http

import application.usecase.RegisterUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import presentation.dto.RegisterRequest

fun Route.authRoutes(
    registerUseCase: RegisterUseCase,
) {
    post("/register") {
        val request = call.receive<RegisterRequest>()

        try {
            registerUseCase.invoke(request.email)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.localizedMessage)
        }

        return@post call.respond(HttpStatusCode.OK)
    }

    post("/verify") {
        TODO("Not yet implemented")
    }

    post("/confirm-registration") {
        TODO("Not yet implemented")
    }
}