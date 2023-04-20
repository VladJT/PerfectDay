package jt.projects.repository.room

import jt.projects.model.DataModel
import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    suspend fun getAll(): Flow<DataModel.ScheduledEvent>

    fun getNotesByDate(date: Long): Flow<List<DataModel.ScheduledEvent>>

    fun getAllNotes(): Flow<List<DataModel.ScheduledEvent>>

    suspend fun getNoteById(id: Int): DataModel.ScheduledEvent

    suspend fun getEventsCountBeforeDate(date: Long): Int

    suspend fun insert(scheduledEvent: DataModel.ScheduledEvent)

    suspend fun update(scheduledEvent: DataModel.ScheduledEvent)

    suspend fun deleteById(id: Int)

    suspend fun deleteBeforeDate(date: Long)

    suspend fun deleteAll()
}