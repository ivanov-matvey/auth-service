package shared

class TooManyRequestsException(
    retryAfterSeconds: Long
) : RuntimeException(
    "Слишком много запросов. Повторите попытку через ${retryAfterSeconds/ 60} минут."
)

class TooManyAttemptsException(
    retryAfterSeconds: Long
) : RuntimeException(
    "Слишком много неудачных попыток. Повторите попытку через ${retryAfterSeconds/ 60} минут."
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
