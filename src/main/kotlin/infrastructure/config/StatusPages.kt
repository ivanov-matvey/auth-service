package infrastructure.config

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import presentation.dto.ErrorResponse
import shared.InvalidAccessTokenException
import shared.InvalidBirthdayException
import shared.InvalidConfirmationCodeException
import shared.InvalidEmailException
import shared.InvalidPasswordException
import shared.InvalidRefreshTokenException
import shared.InvalidRegistrationTokenException
import shared.TooManyAttemptsException
import shared.TooManyRequestsException
import shared.UserAlreadyExistsException
import shared.UserNotFoundException

fun Application.configureStatusPages() {
    install(StatusPages) {
        status(HttpStatusCode.NotFound) { call, status ->
            call.respond(
                HttpStatusCode.NotFound,
                ErrorResponse(
                    title = "Страница не найдена.",
                    status = 404
                )
            )
        }

        exception<TooManyRequestsException> { call, cause ->
            call.respond(
                HttpStatusCode.TooManyRequests,
                ErrorResponse(
                    title = cause.message ?: "Слишком много запросов.",
                    status = 429
                )
            )
        }

        exception<TooManyAttemptsException> { call, cause ->
            call.respond(
                HttpStatusCode.TooManyRequests,
                ErrorResponse(
                    title = cause.message ?: "Слишком много попыток.",
                    status = 429
                )
            )
        }

        exception<UserAlreadyExistsException> { call, cause ->
            call.respond(
                HttpStatusCode.Conflict,
                ErrorResponse(
                    title = cause.message ?: "Пользователь уже существует.",
                    status = 409,
                    errors = mapOf(
                        "email" to (cause.message ?: "Пользователь уже существует.")
                    )
                )
            )
        }

        exception<InvalidRegistrationTokenException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(
                    title = cause.message ?: "Неверный токен регистрации.",
                    status = 400
                )
            )
        }

        exception<InvalidConfirmationCodeException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(
                    title = cause.message ?: "Неверный код подтверждения.",
                    status = 400,
                    errors = mapOf(
                        "code" to (cause.message ?: "Неверный код подтверждения.")
                    )
                )
            )
        }

        exception<InvalidEmailException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(
                    title = cause.message ?: "Неверный адрес электронной почты.",
                    status = 400,
                    errors = mapOf(
                        "email" to (cause.message ?: "Неверный адрес электронной почты.")
                    )
                )
            )
        }

        exception<UserNotFoundException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(
                    title = cause.message ?: "Пользователь не найден.",
                    status = 400,
                    errors = mapOf(
                        "email" to (cause.message ?: "Пользователь не найден.")
                    )
                )
            )
        }

        exception<InvalidPasswordException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(
                    title = cause.message ?: "Неверный пароль.",
                    status = 400,
                    errors = mapOf(
                        "password" to (cause.message ?: "Неверный пароль.")
                    )
                )
            )
        }

        exception<InvalidRefreshTokenException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(
                    title = cause.message ?: "Неверный токен обновления.",
                    status = 400
                )
            )
        }

        exception<InvalidAccessTokenException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(
                    title = cause.message ?: "Неверный токен доступа.",
                    status = 400
                )
            )
        }

        exception<InvalidBirthdayException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(
                    title = cause.message ?: "Неверная дата рождения.",
                    status = 400
                )
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
