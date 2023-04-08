package jt.projects.repository.room


import jt.projects.model.DataModel
import jt.projects.repository.room.mapper.toScheduledEvent
import jt.projects.repository.room.mapper.toRoomEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map

class RoomDatabaseImpl(private val dao: ScheduledEventDao) : LocalRepository {

    override suspend fun getAll(): Flow<DataModel.ScheduledEvent> =
        dao.getAll().asFlow().map { it.toScheduledEvent() }


    override suspend fun insert(scheduledEvent: DataModel.ScheduledEvent) =
        dao.insert(scheduledEvent.toRoomEntity())

    override suspend fun update(scheduledEvent: DataModel.ScheduledEvent) =
        dao.update(scheduledEvent.toRoomEntity())
}

