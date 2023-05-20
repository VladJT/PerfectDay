package jt.projects.perfectday.presentation.today.adapter.birth

import jt.projects.model.FriendType
import java.time.LocalDate

data class FriendItem(
    val id: String,
    val type: FriendType,
    val name: String,
    val birthDate: LocalDate,
    val age: Int,
    val photoUrl: String
)