package jt.projects.repository.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate


@Entity
class ScheduledEventEntity(

    @field:PrimaryKey(autoGenerate = true)
    @field:ColumnInfo(name = "id")
    var id: Int,

    @field: ColumnInfo(name = "name")
    val name: String,

    @field: ColumnInfo(name = "description")
    val description: String,

    @field: ColumnInfo(name = "date")
    val date: String
)
