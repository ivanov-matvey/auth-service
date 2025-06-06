package infrastructure.config

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respondText
import shared.InvalidConfirmationCodeException
import shared.InvalidEmailException
import shared.InvalidRegistrationTokenException
import shared.TooManyAttemptsException
import shared.TooManyRequestsException
import shared.UserAlreadyExistsException

fun Application.configureStatusPages() {
    install(StatusPages) {
        status(HttpStatusCode.NotFound) { call, status ->
            call.respondText(
                text = "Страница не найдена.",
                status = status
            )
        }

        exception<TooManyRequestsException> { call, cause ->
            call.respondText(
                text = cause.message ?: "Слишком много запросов.",
                status = HttpStatusCode.TooManyRequests
            )
        }

        exception<TooManyAttemptsException> { call, cause ->
            call.respondText(
                text = cause.message ?: "Слишком много попыток.",
                status = HttpStatusCode.TooManyRequests
            )
        }

        exception<UserAlreadyExistsException> { call, cause ->
            call.respondText(
                text = cause.message ?: "Пользователь уже существует.",
                status = HttpStatusCode.Conflict
            )
        }

        exception<InvalidRegistrationTokenException> { call, cause ->
            call.respondText(
                text = cause.message ?: "Неверный токен регистрации.",
                status = HttpStatusCode.BadRequest
            )
        }

        exception<InvalidConfirmationCodeException> { call, cause ->
            call.respondText(
                text = cause.message ?: "Неверный код подтверждения.",
                status = HttpStatusCode.BadRequest
            )
        }

        exception<InvalidEmailException> { call, cause ->
            call.respondText(
                text = cause.message ?: "Неверный адрес электронной почты.",
                status = HttpStatusCode.BadRequest
            )
        }

        exception<Throwable> { call, cause ->
            call.respondText(
                text = cause.localizedMessage,
                status = HttpStatusCode.InternalServerError
            )
        }
    }
}
