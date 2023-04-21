package jt.projects.perfectday.presentation.schedule_event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import jt.projects.model.DataModel
import jt.projects.perfectday.interactors.ScheduledEventInteractorImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.time.LocalDate

class ScheduleEventViewModel(
    private val scheduledEventInteractor: ScheduledEventInteractorImpl
) : ViewModel() {

    private val liveData: MutableLiveData<DataModel.ScheduledEvent> = MutableLiveData()

    val liveDataForViewToObserve: LiveData<DataModel.ScheduledEvent>
        get() {
            return liveData
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
}