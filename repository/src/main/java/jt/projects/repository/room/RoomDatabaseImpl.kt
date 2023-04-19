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

    override fun getAllNotes(): Flow<List<DataModel.ScheduledEvent>> =
        dao.getAllNotes().map { list -> list.map(ScheduledEventEntity::toScheduledEvent)  }

    override fun getNotesByDate(date: Long): Flow<List<DataModel.ScheduledEvent>> =
        dao.getNotesByDate(date)
            .map { list -> list.map(ScheduledEventEntity::toScheduledEvent) }

    override suspend fun getEventsCountBeforeDate(date: Long): Int =
        dao.getEventsCountBeforeDate(date)

    override suspend fun insert(scheduledEvent: DataModel.ScheduledEvent) =
        dao.insert(scheduledEvent.toRoomEntity())

    override suspend fun update(scheduledEvent: DataModel.ScheduledEvent) =
        dao.update(scheduledEvent.toRoomEntity())

    override suspend fun deleteById(id: Int) = dao.deleteById(id)

    override suspend fun deleteBeforeDate(date: Long) = dao.deleteBeforeDate(date)

    override suspend fun deleteAll() = dao.deleteAll()
}