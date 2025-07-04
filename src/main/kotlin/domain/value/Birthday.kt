package domain.value

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import shared.InvalidBirthdayException

class Birthday private constructor(val value: LocalDate) {

    companion object {
        fun parse(raw: String): Birthday {
            try {
                val date = LocalDate.parse(raw)
                val today = Clock.System.now().toLocalDateTime(
                    TimeZone.currentSystemDefault()
                ).date
                if (date > today) {
                    throw InvalidBirthdayException()
                }
                return Birthday(date)
            } catch (_: Exception) {
                throw InvalidBirthdayException()
            }
        }
    }

    override fun toString(): String = value.toString()
}
