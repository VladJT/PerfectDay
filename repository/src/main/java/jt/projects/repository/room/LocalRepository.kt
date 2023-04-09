package jt.projects.repository.room

import jt.projects.model.DataModel
import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    suspend fun getAll(): Flow<DataModel.ScheduledEvent>

    suspend fun insert(scheduledEvent: DataModel.ScheduledEvent)

    suspend fun update(scheduledEvent: DataModel.ScheduledEvent)

    suspend fun deleteById(id: Int)
    suspend fun deleteAll()
}