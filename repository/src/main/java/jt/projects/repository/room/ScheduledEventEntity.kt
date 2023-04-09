package jt.projects.repository.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class ScheduledEventEntity(
    @field:PrimaryKey(autoGenerate = true)
    @field: ColumnInfo(name = "id")
    var id: Int = 0,

    @field: ColumnInfo(name = "name")
    val name: String,

    @field: ColumnInfo(name = "description")
    val description: String,

    @field: ColumnInfo(name = "date")
    val date: String
)
