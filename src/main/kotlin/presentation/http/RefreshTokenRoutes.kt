package presentation.http

import application.service.RefreshTokenService
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import presentation.dto.RefreshTokenRequest
import presentation.dto.RefreshTokenResponse

fun Route.refreshTokenRoutes(
    refreshTokenService: RefreshTokenService
) {
    post {
        val request = call.receive<RefreshTokenRequest>()

        val accessToken = refreshTokenService(request.refreshToken)

        call.respond(HttpStatusCode.OK, RefreshTokenResponse(accessToken))
    }
}
