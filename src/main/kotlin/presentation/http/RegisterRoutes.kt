package presentation.http

import application.service.RegisterByCodeService
import application.service.RegisterByCodeVerifyService
import application.usecase.RegisterConfirmUseCase
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
    registerByCodeService: RegisterByCodeService,
    registerByCodeVerifyService: RegisterByCodeVerifyService,
    registerConfirmUseCase: RegisterConfirmUseCase
) {
    post {
        val request = call.receive<RegisterRequest>()

        registerByCodeService(request.email)

        return@post call.respond(HttpStatusCode.OK)
    }

    post("/verify") {
        val request = call.receive<CodeVerifyRequest>()

        val token = registerByCodeVerifyService(
            request.email,
            request.code
        ).token
        call.response.header(
            name = "token",
            value = token.toString()
        )

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