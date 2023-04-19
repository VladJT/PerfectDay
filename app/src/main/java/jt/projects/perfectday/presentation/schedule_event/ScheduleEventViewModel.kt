package jt.projects.perfectday.presentation.schedule_event

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jt.projects.model.DataModel
import jt.projects.perfectday.core.AppDataCache
import jt.projects.perfectday.core.extensions.createMutableSingleEventFlow
import jt.projects.utils.toStdLocalDate
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class ScheduleEventViewModel(
    private val dataCache: AppDataCache
) : ViewModel() {
    private val _isCloseDialog = createMutableSingleEventFlow<Boolean>()
    val isCloseDialog get() = _isCloseDialog.asSharedFlow()

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

        liveData.postValue(newData)
    }

    // сохраняем изменения в базу данных
    fun saveData() {
        liveDataForViewToObserve.value?.let {
            CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
                if (it.id == 0) {
                    dataCache.insertScheduledEvent(it)
                } else {
                    dataCache.updateScheduledEvent(it)
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

        viewModelScope.launch(Dispatchers.IO) {
            try {
                dataCache.insertScheduledEvent(note)
                _isCloseDialog.tryEmit(true)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("ScheduleEventViewModel", "$e")
            }
        }
    }
}