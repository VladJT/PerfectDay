package jt.projects.repository.room


import jt.projects.model.DataModel
import jt.projects.repository.room.mapper.toRoomEntity
import jt.projects.repository.room.mapper.toScheduledEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map

class RoomDatabaseImpl(private val dao: ScheduledEventDao) : LocalRepository {

    override suspend fun getAll(): Flow<DataModel.ScheduledEvent> =
        dao.getAll().asFlow().map { it.toScheduledEvent() }

    override fun getNotesByDate(date: String): Flow<List<DataModel.ScheduledEvent>> =
        dao.getNotesByDate(date)
            .map { list -> list.map(ScheduledEventEntity::toScheduledEvent) }

    override suspend fun insert(scheduledEvent: DataModel.ScheduledEvent) =
        dao.insert(scheduledEvent.toRoomEntity())

    override suspend fun update(scheduledEvent: DataModel.ScheduledEvent) =
        dao.update(scheduledEvent.toRoomEntity())

    override suspend fun deleteById(id: Int) =
        dao.deleteById(id)

    override suspend fun deleteAll() =
        dao.deleteAll()
}