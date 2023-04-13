package jt.projects.perfectday.interactors


import jt.projects.model.DataModel
import jt.projects.repository.room.LocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ScheduledEventInteractorImpl(
    private val repository: LocalRepository
) {
    suspend fun getAll(): List<DataModel.ScheduledEvent> =
        repository.getAll().toList()

    suspend fun getScheduledEventsByDate(date: LocalDate): List<DataModel.ScheduledEvent> {
        return repository
            .getAll()
            .toList()
            .filter { it.date == date }
    }

    suspend fun getScheduledEventsByPeriod(
        startDate: LocalDate,
        endDate: LocalDate
    ): List<DataModel.ScheduledEvent> {
        return repository
            .getAll()
            .toList()
            .filter { it.date in startDate..endDate }
            .sortedBy { it.date }
    }

    fun getNotesByDate(date: LocalDate): Flow<List<DataModel.ScheduledEvent>> {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val dateFormat = date.format(formatter)
        return repository.getNotesByDate(dateFormat)
    }

    suspend fun insert(scheduledEvent: DataModel.ScheduledEvent) =
        repository.insert(scheduledEvent)

    suspend fun update(scheduledEvent: DataModel.ScheduledEvent) =
        repository.update(scheduledEvent)

    suspend fun deleteScheduledEventById(id: Int) =
        repository.deleteById(id)

    suspend fun deleteAll() =
        repository.deleteAll()
}