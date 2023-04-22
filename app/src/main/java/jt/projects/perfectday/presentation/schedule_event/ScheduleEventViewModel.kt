package jt.projects.perfectday.presentation.schedule_event

import android.util.Log
import androidx.lifecycle.*
import jt.projects.model.DataModel
import jt.projects.perfectday.core.extensions.*
import jt.projects.perfectday.interactors.ScheduledEventInteractorImpl
import jt.projects.utils.toStdLocalDate
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asSharedFlow
import java.time.LocalDate

class ScheduleEventViewModel(
    private val scheduledEventInteractor: ScheduledEventInteractorImpl
) : ViewModel() {
    private val _isCloseFragment = createMutableSingleEventFlow<Boolean>()
    val isCloseFragment get() = _isCloseFragment.asSharedFlow()

    private val _note = MutableLiveData<DataModel.ScheduledEvent>()
    val note get() = _note

    private val liveData: MutableLiveData<DataModel.ScheduledEvent> = MutableLiveData()
    val liveDataForViewToObserve: LiveData<DataModel.ScheduledEvent>
        get() {
            return liveData
        }

    fun getNote(id: Int?) {
        if (id == null) return
        launchOrError(
            Dispatchers.IO,
            action = { _note.postValue(scheduledEventInteractor.getNoteById(id)) },
            error= { Log.e(this.javaClass.simpleName, "$it")}
        )
    }

    fun setData(data: DataModel.ScheduledEvent) {
        liveData.postValue(data)
    }

    fun updateData(name: String? = null, description: String? = null, date: LocalDate? = null) {
        val newData = liveDataForViewToObserve.value
        name?.let { newData?.name = it }
        description?.let { newData?.description = it }
        date?.let { newData?.date = it }

        liveData.postValue(newData!!)
    }

    // сохраняем изменения в базу данных
    fun saveData() {
        liveDataForViewToObserve.value?.let {
            CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
                if (it.id == 0) {
                    scheduledEventInteractor.insert(it)
                } else {
                    scheduledEventInteractor.update(it)
                }
            }
        }
    }

    fun saveData(headerNote: String, description: String, date: String) {
        val note = DataModel.ScheduledEvent(
            id = 0,
            name = headerNote,
            date = date.toStdLocalDate(),
            description = description
        )

        launchOrError(
            Dispatchers.IO,
            action = {
                scheduledEventInteractor.insert(note)
                _isCloseFragment.tryEmit(true)
            },
            error = { Log.e(this.javaClass.simpleName, "$it") }
        )
    }
}