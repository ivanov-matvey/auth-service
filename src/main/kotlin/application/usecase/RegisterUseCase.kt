package application.usecase

import domain.repository.UserRepository

class RegisterUseCase(
    private val repository: UserRepository
) {
    operator fun invoke(email: String): Result<Unit> {
        if (!isValidEmail(email)) {
            return Result.failure(IllegalArgumentException("Invalid email"))
        }

        val user = repository.findByEmail(email)
        if (user != null) {
            return Result.failure(IllegalArgumentException("User already exists"))
        }

        return Result.success(Unit)
    }

    private fun isValidEmail(email: String): Boolean {
        val regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return regex.matches(email)
    }
}