package jt.projects.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate


sealed class DataModel {

    data class BirthdayFromVk(
        val vkId: Long,
        val name: String,
        val birthDate: LocalDate,
        val age: Int,
        val photoUrl: String
    ) : DataModel()

    data class BirthdayFromPhone(
        val id: String,
        val name: String,
        val birthDate: LocalDate,
        val age: Int?,
        val photoUri: String?
    ) : DataModel()

    data class SimpleNotice(
        val name: String,
        var description: String
    ) : DataModel() {
        companion object {
            val EMPTY = SimpleNotice("", "")
        }
    }

    data class Holiday(
        var name: String,
        val country: String,
        val type: String,
        var description: String?,
        val date: LocalDate
    ) : DataModel() {
        companion object {
            val EMPTY = Holiday("", "", "", "", LocalDate.MIN)
            val CURRENT_DATE = Holiday("Сегодня праздников нет", "", "", "", LocalDate.now())
        }
    }

    @Parcelize
    data class ScheduledEvent(
        val id: Int,
        var name: String,
        var date: LocalDate,
        var description: String,
    ) : DataModel(), Parcelable
}
