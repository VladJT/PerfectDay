package jt.projects.perfectday.presentation.today.adapter.note

import java.time.LocalDate

data class NoteItem(
    val id: Int,
    var name: String,
    var date: LocalDate,
    var description: String
)