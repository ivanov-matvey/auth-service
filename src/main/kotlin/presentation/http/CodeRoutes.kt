package presentation.http

import application.usecase.CodeSendUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import presentation.dto.RegisterRequest

fun Route.codeRoutes(
    codeSendUseCase: CodeSendUseCase
) {
    post {
        val request = call.receive<RegisterRequest>()

        codeSendUseCase.invoke(request.email)

        return@post call.respond(HttpStatusCode.OK)
    }
}