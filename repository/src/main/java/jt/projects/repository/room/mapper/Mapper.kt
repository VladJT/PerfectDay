package jt.projects.repository.room.mapper

import jt.projects.model.DataModel
import jt.projects.repository.room.ScheduledEventEntity
import java.time.LocalDate

fun ScheduledEventEntity.toScheduledEvent(): DataModel.ScheduledEvent {
    return DataModel.ScheduledEvent(
        id = this.id,
        name = this.name,
        description = this.description,
        date = LocalDate.ofEpochDay(this.date)
    )
}

fun DataModel.ScheduledEvent.toRoomEntity(): ScheduledEventEntity {
    return ScheduledEventEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        date = this.date.toEpochDay()
    )
}