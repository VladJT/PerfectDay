package jt.projects.perfectday.interactors


import jt.projects.model.DataModel
import jt.projects.repository.room.LocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class ScheduledEventInteractorImpl(
    private val repository: LocalRepository
) {
    fun getAllNotes(): Flow<List<DataModel.ScheduledEvent>> =
        repository.getAllNotes()

    suspend fun getScheduledEventsByDate(date: LocalDate): List<DataModel.ScheduledEvent> {
        val result = mutableListOf<DataModel.ScheduledEvent>()
        getNotesByDate(date).map {
            result.addAll(it)
        }
        return result
    }

    fun getScheduledEventsByPeriod(
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<DataModel.ScheduledEvent>> {
        return repository
            .getAllNotes()
            .map {
                it.filter { it.date in startDate..endDate }
            }
            .map { it -> it.sortedBy { it.date } }
    }

    fun getNotesByDate(date: LocalDate): Flow<List<DataModel.ScheduledEvent>> {
        //  val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        //  val dateFormat = date.format(formatter)
        return repository.getNotesByDate(date.toEpochDay())
    }

    suspend fun insert(scheduledEvent: DataModel.ScheduledEvent) =
        repository.insert(scheduledEvent)

    suspend fun update(scheduledEvent: DataModel.ScheduledEvent) =
        repository.update(scheduledEvent)

    suspend fun deleteScheduledEventById(id: Int) =
        repository.deleteById(id)

    suspend fun getScheduledEventCountBeforeDate(date: LocalDate): Int =
        repository.getEventsCountBeforeDate(date.toEpochDay())

    suspend fun deleteScheduledEventBeforeDate(date: LocalDate) =
        repository.deleteBeforeDate(date.toEpochDay())

    suspend fun deleteAll() =
        repository.deleteAll()
}