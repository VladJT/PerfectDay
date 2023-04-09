package jt.projects.repository.room.mapper

import jt.projects.model.DataModel
import jt.projects.repository.room.ScheduledEventEntity
import jt.projects.utils.toStdFormatString
import jt.projects.utils.toStdLocalDate

fun ScheduledEventEntity.toScheduledEvent(): DataModel.ScheduledEvent {
    return DataModel.ScheduledEvent(
        id = this.id,
        name = this.name,
        description = this.description,
        date = this.date.toStdLocalDate()
    )
}

fun DataModel.ScheduledEvent.toRoomEntity(): ScheduledEventEntity {
    return ScheduledEventEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        date = this.date.toStdFormatString()
    )
}