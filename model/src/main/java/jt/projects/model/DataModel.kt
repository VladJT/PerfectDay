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
    ) : DataModel()

    data class Holiday(
        var name: String,
        val country: String,
        val type: String,
        var description: String?,
        val date: LocalDate
    ) : DataModel()

    @Parcelize
    data class ScheduledEvent(
        val id: Int,
        var name: String,
        var date: LocalDate,
        var description: String,
    ) : DataModel(), Parcelable

    data class Friend(
        val id: String,
        val type: FriendType,
        val name: String,
        val birthDate: LocalDate,
        val age: Int,
        val photoUrl: String
    ) : DataModel()
}
