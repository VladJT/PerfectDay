package jt.projects.model

<<<<<<< HEAD
=======
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
>>>>>>> eacebb512b34ed4adb003fc3f9bb1cb2cd3963d1
import java.time.LocalDate


sealed class DataModel {

    data class BirthdayFromVk(
        val name: String,
        val birthDate: LocalDate,
<<<<<<< HEAD
        val age: Int?,
        val photoUrl: String?
=======
        val age: Int,
        val photoUrl: String
>>>>>>> eacebb512b34ed4adb003fc3f9bb1cb2cd3963d1
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

<<<<<<< HEAD
    data class ScheduledEvent(
        val id: Int,
        val name: String,
        val date: LocalDate,
        val description: String,
    ) : DataModel()
=======
    @Parcelize
    data class ScheduledEvent(
        val id: Int,
        var name: String,
        var date: LocalDate,
        var description: String,
    ) : DataModel(), Parcelable
>>>>>>> eacebb512b34ed4adb003fc3f9bb1cb2cd3963d1
}
