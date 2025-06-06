package presentation.http

import application.service.LoginService
import application.usecase.CodeSendUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import presentation.dto.CodeVerifyRequest
import presentation.dto.LoginRequest

fun Route.loginRoutes(
    loginService: LoginService,
    codeSendUseCase: CodeSendUseCase,
) {
    post("/code") {
        val request = call.receive<LoginRequest>()

        codeSendUseCase(request.email)

        return@post call.respond(HttpStatusCode.OK)

    }

    post("/verify") {
        val request = call.receive<CodeVerifyRequest>()

        val tokens = loginService.getTokensByCode(request.email, request.code)
        call.response.header(name = "accessToken", value = tokens.accessToken)
        call.response.header(name = "refreshToken", value = tokens.refreshToken)
        return@post call.respond(HttpStatusCode.OK)
    }

    post("/password") {
        TODO("Not implemented yet")
    }
}