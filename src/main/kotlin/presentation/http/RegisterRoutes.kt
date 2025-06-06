package presentation.http

import application.usecase.RegisterConfirmUseCase
import application.usecase.RegisterVerifyUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import presentation.dto.RegisterConfirmRequest
import presentation.dto.RegisterVerifyRequest
import presentation.mapper.modelToDto

fun Route.registerRoutes(
    registerVerifyUseCase: RegisterVerifyUseCase,
    registerConfirmUseCase: RegisterConfirmUseCase,
) {
    post("/verify") {
        val request = call.receive<RegisterVerifyRequest>()

        val token = registerVerifyUseCase.invoke(request.email, request.code)
        call.response.header(
            name = "token",
            value = token.toString()
        )
        return@post call.respond(HttpStatusCode.OK)
    }

    post("/confirm") {
        val request = call.receive<RegisterConfirmRequest>()

        val user = registerConfirmUseCase.invoke(
            request.token,
            request.fullName,
            request.birthday,
            request.password
        )

        return@post call.respond(HttpStatusCode.OK, modelToDto(user))
    }
}