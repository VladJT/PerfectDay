package jt.projects.repository.room.mapper

import jt.projects.model.DataModel
import jt.projects.repository.room.ScheduledEventEntity
import jt.projects.utils.extensions.emptyString
import java.time.LocalDate

fun ScheduledEventEntity?.toScheduledEvent(): DataModel.ScheduledEvent {
    if (this == null) return DataModel.ScheduledEvent(0, emptyString(), LocalDate.now(), emptyString())

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