package presentation.http

import application.service.LoginByCodeService
import application.service.LoginByCodeVerifyService
import application.service.LoginByPasswordService
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import presentation.dto.CodeVerifyRequest
import presentation.dto.LoginByCodeRequest
import presentation.dto.LoginByPasswordRequest

fun Route.loginRoutes(
    loginByCodeService: LoginByCodeService,
    loginByCodeVerifyService: LoginByCodeVerifyService,
    loginByPasswordService: LoginByPasswordService
) {
    post("/code") {
        val request = call.receive<LoginByCodeRequest>()

        loginByCodeService(request.email)

        return@post call.respond(HttpStatusCode.OK)

    }

    post("/verify") {
        val request = call.receive<CodeVerifyRequest>()

        val tokens = loginByCodeVerifyService(
            request.email,
            request.code
        )
        call.response.header(
            name = "accessToken",
            value = tokens.accessToken
        )
        call.response.header(
            name = "refreshToken",
            value = tokens.refreshToken
        )

        return@post call.respond(HttpStatusCode.OK)
    }

    post("/password") {
        val request = call.receive<LoginByPasswordRequest>()

        loginByPasswordService(
            request.email,
            request.password
        )

        return@post call.respond(HttpStatusCode.OK)
    }
}