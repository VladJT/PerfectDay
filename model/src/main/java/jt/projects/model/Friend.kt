package jt.projects.model

import java.time.LocalDate

data class Friend(
    val idVk: Long?,
    val idPhone: String?,
    val type: FriendType,
    val name: String,
    val birthDate: LocalDate,
    val age: Int,
    val photoUrl: String
)