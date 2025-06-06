package presentation.http

import application.usecase.RegisterUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import presentation.dto.RegisterRequest

fun Route.codeRoutes(
    registerUseCase: RegisterUseCase
) {
    post {
        val request = call.receive<RegisterRequest>()

        registerUseCase.invoke(request.email)

        return@post call.respond(HttpStatusCode.OK)
    }
}