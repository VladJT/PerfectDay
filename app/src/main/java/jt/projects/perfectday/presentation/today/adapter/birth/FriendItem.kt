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
) {
    companion object {
        val LOADING = FriendItem("-1", FriendType.PHONE, "loading", LocalDate.MIN, 0, "loading")
        val EMPTY = FriendItem("", FriendType.PHONE, "", LocalDate.MIN, 0, "")
    }
}