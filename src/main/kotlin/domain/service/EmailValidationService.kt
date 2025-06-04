package domain.service

class EmailValidationService {
    private val regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()

    fun isValid(email: String): Boolean {
        return regex.matches(email)
    }
}
