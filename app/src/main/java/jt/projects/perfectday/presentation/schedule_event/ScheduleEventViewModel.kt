package jt.projects.perfectday.presentation.schedule_event

import android.util.Log
import androidx.lifecycle.*
import jt.projects.model.DataModel
import jt.projects.perfectday.core.extensions.*
import jt.projects.perfectday.interactors.ScheduledEventInteractorImpl
import jt.projects.perfectday.core.toStdLocalDate
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asSharedFlow

class ScheduleEventViewModel(
    private val scheduledEventInteractor: ScheduledEventInteractorImpl
) : ViewModel() {
    private val _isCloseFragment = createMutableSingleEventFlow<Boolean>()
    val isCloseFragment get() = _isCloseFragment.asSharedFlow()

    private val _note = MutableLiveData<DataModel.ScheduledEvent>()
    val note: LiveData<DataModel.ScheduledEvent> get() = _note

    fun getNote(id: Int?) {
        if (id == null || id == -1) return
        launchOrError(
            Dispatchers.IO,
            action = { _note.postValue(scheduledEventInteractor.getNoteById(id)) },
            error= { Log.e(this.javaClass.simpleName, "$it")}
        )
    }

    fun saveOrUpdateNote(headerNote: String, description: String, date: String) {
        val scheduleEvent = if (note.value == null)
            getNote(headerNote, description, date)
        else
            getNote(headerNote, description, date, note.value!!.id)

        if (scheduleEvent.id == 0)
            saveNote(scheduleEvent)
        else
            updateNote(scheduleEvent)
    }

    private fun getNote(headerNote: String, description: String, date: String, id: Int = 0) =
        DataModel.ScheduledEvent(
            id = id,
            name = headerNote,
            date = date.toStdLocalDate(),
            description = description
        )

    private fun saveNote(note: DataModel.ScheduledEvent) {
        launchOrError(
            Dispatchers.IO,
            action = {
                scheduledEventInteractor.insert(note)
                _isCloseFragment.tryEmit(true)
            },
            error = { Log.e(this.javaClass.simpleName, "$it") }
        )
    }

    private fun updateNote(note: DataModel.ScheduledEvent) {
        launchOrError(
            Dispatchers.IO,
            action = {
                scheduledEventInteractor.update(note)
                _isCloseFragment.tryEmit(true)
            },
            error = { Log.e(this.javaClass.simpleName, "$it") }
        )
    }
}