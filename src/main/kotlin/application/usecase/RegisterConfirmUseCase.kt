package application.usecase

import domain.model.User
import domain.repository.UserRepository
import domain.service.RedisService
import kotlinx.datetime.LocalDate
import org.mindrot.jbcrypt.BCrypt
import shared.InvalidRegistrationTokenException
import java.util.UUID

class RegisterConfirmUseCase(
    private val redisService: RedisService,
    private val userRepository: UserRepository
) {
    operator fun invoke(
        token: String,
        fullName: String,
        birthday: String,
        password: String,
    ): User {
        val registerKey = "register:token:$token"

        val email = redisService.get(registerKey)
        if (email == null) {
            throw InvalidRegistrationTokenException()
        }

        val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
        val user = User(
            id = UUID.randomUUID(),
            email = email,
            password = hashedPassword,
            fullName = fullName,
            birthday = LocalDate.parse(birthday)
        )
        userRepository.save(user)

        redisService.del(registerKey)

        return user
    }
}