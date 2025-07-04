package domain.value

import shared.InvalidEmailException

class Email private constructor(val value: String) {

    companion object {
        private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")

        fun create(raw: String): Email {
            val trimmed = raw.trim()
            if (!EMAIL_REGEX.matches(trimmed)) {
                throw InvalidEmailException()
            }
            return Email(trimmed)
        }
    }

    override fun toString() = value
}
