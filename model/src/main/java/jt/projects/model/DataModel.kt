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
        val name: String,
        val birthDate: LocalDate,
        val age: Int?,
        val photoUrl: String?
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
        val photoUrl: String?
    ) : DataModel()

    @Parcelize
    data class ScheduledEvent(
        val id: Int,
        val name: String,
        var date: LocalDate,
        val description: String,
    ) : DataModel(), Parcelable
}
