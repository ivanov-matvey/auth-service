package presentation.http

import application.service.RegisterService
import application.usecase.CodeSendUseCase
import application.usecase.RegisterConfirmUseCase
import application.usecase.UserExistsUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import presentation.dto.RegisterConfirmRequest
import presentation.dto.CodeVerifyRequest
import presentation.dto.RegisterRequest
import presentation.mapper.modelToDto

fun Route.registerRoutes(
    registerService: RegisterService,
    registerConfirmUseCase: RegisterConfirmUseCase,
    userExistsUseCase: UserExistsUseCase,
    codeSendUseCase: CodeSendUseCase
) {
    post {
        val request = call.receive<RegisterRequest>()

        userExistsUseCase(request.email)
        codeSendUseCase(request.email)

        return@post call.respond(HttpStatusCode.OK)
    }

    post("/verify") {
        val request = call.receive<CodeVerifyRequest>()

        val token = registerService.getToken(request.email, request.code)
        call.response.header(name = "token", value = token.toString())
        return@post call.respond(HttpStatusCode.OK)
    }

    post("/confirm") {
        val request = call.receive<RegisterConfirmRequest>()

        val user = registerConfirmUseCase(
            request.token,
            request.fullName,
            request.birthday,
            request.password
        )

        return@post call.respond(HttpStatusCode.OK, modelToDto(user))
    }
}