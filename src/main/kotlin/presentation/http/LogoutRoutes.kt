package presentation.http

import application.service.LogoutService
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import presentation.dto.LogoutRequest

fun Route.logoutRoutes(
    logoutService: LogoutService
) {
    post {
        val accessHeader = call.request.headers["Authorization"]
        val accessToken = accessHeader?.removePrefix("Bearer ")?.trim()

        val request = call.receive<LogoutRequest>()

        logoutService(accessToken, request.refreshToken)

        call.respond(HttpStatusCode.OK)
    }
}
