package application.usecase

import domain.model.User
import domain.repository.UserRepository
import domain.service.RedisService
import domain.value.Birthday
import domain.value.Password
import shared.InvalidRegistrationTokenException
import java.util.UUID

class RegisterConfirmUseCase(
    private val redisService: RedisService,
    private val userRepository: UserRepository
) {
    operator fun invoke(
        token: String,
        fullName: String,
        birthdayRaw: String,
        passwordRaw: String,
    ): User {
        val registerKey = "register:token:$token"

        val email = redisService.get(registerKey)
        if (email == null) {
            throw InvalidRegistrationTokenException()
        }

        val birthday = Birthday.parse(birthdayRaw)
        val password = Password.fromPlain(passwordRaw)

        val hashedPassword = password.hashed

        val user = User(
            id = UUID.randomUUID(),
            email = email,
            password = hashedPassword,
            fullName = fullName,
            birthday = birthday.value
        )
        userRepository.save(user)

        redisService.del(registerKey)

        return user
    }
}