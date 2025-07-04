package domain.value

import org.mindrot.jbcrypt.BCrypt
import shared.InvalidPasswordException

class Password private constructor(val hashed: String) {

    companion object {
        private const val MIN_LENGTH = 8

        fun fromPlain(raw: String): Password {
            if (raw.length < MIN_LENGTH) {
                throw InvalidPasswordException()
            }

            val hashed = BCrypt.hashpw(raw, BCrypt.gensalt())
            return Password(hashed)
        }

        fun verify(raw: String, hashed: String): Boolean =
            BCrypt.checkpw(raw, hashed)
    }
}
