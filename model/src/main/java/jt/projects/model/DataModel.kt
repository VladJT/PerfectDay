package jt.projects.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate


sealed class DataModel {

    data class BirthdayFromVk(
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
        val description: String
    ) : DataModel()

    data class Holiday(
        val name: String,
        val country: String,
        val type: String,
        val description: String?,
        val date: LocalDate
    ) : DataModel()

    @Parcelize
    data class ScheduledEvent(
        val id: Int,
        var name: String,
        var date: LocalDate,
        var description: String,
    ) : DataModel(), Parcelable
}
