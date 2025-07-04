package shared

class TooManyRequestsException(
    retryAfterSeconds: Long
) : RuntimeException(
    "Слишком много запросов. Повторите попытку через ${retryAfterSeconds/ 60} минут(ы)."
)

class TooManyAttemptsException(
    retryAfterSeconds: Long
) : RuntimeException(
    "Слишком много неудачных попыток. Повторите попытку через ${retryAfterSeconds/ 60} минут(ы)."
)

class InvalidRegistrationTokenException() : RuntimeException(
    "Неверный токен регистрации."
)

class InvalidConfirmationCodeException() : RuntimeException(
    "Неверный код подтверждения."
)

class InvalidEmailException() : RuntimeException(
    "Неверный адрес электронной почты."
)

class UserAlreadyExistsException() : RuntimeException(
    "Пользователь с такой электронной почтой уже существует."
)

class UserNotFoundException() : RuntimeException(
    "Пользователь не существует."
)

class InvalidPasswordException() : RuntimeException(
    "Неверный пароль."
)

class InvalidRefreshTokenException() : RuntimeException(
    "Неверный токен обновления."
)

class InvalidAccessTokenException() : RuntimeException(
    "Неверный токен доступа."
)

class InvalidBirthdayException() : RuntimeException(
    "Неверная дата рождения."
)
