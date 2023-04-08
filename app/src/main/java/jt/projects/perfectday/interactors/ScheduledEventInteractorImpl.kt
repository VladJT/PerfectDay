package jt.projects.perfectday.interactors


import jt.projects.model.DataModel
import jt.projects.repository.retrofit.facts.FactsRepository
import jt.projects.repository.room.LocalRepository
import jt.projects.repository.room.RoomDatabaseImpl
import kotlinx.coroutines.flow.toList
import java.time.LocalDate

class ScheduledEventInteractorImpl(
    private val repository: LocalRepository
) {
    suspend fun insert(scheduledEvent: DataModel.ScheduledEvent) =
        repository.insert(scheduledEvent)

    suspend fun getAll(): List<DataModel.ScheduledEvent> {
        return repository.getAll().toList()
    }
}