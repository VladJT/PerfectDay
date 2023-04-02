package jt.projects.model

import java.time.LocalDate


sealed class DataModel {

    data class BirthdayFromVk(
        val name: String,
        val birthDate: LocalDate,
        val age: Int?,
        val photoUrl: String?
    ) : DataModel()

    data class BirthdayFromPhone(
        val name: String,
        val birthDate: LocalDate,
        val age: Int?,
        val photoUrl: String?
    ) : DataModel()

    data class Fact(
        val description: String
    ) : DataModel()

    data class Holiday(
        val name: String,
        val country: String,
        val type: String,
        val description: String?,
        val photoUrl: String?
    ) : DataModel()
}


