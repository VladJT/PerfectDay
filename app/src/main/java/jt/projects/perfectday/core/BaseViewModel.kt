package jt.projects.perfectday.core

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import jt.projects.model.AppState
import jt.projects.utils.LOG_TAG

//Создадим базовую ViewModel, куда вынесем общий для всех функционал
abstract class BaseViewModel<T : AppState> : ViewModel() {
    protected val liveData: MutableLiveData<T> = MutableLiveData()

    val liveDataForViewToObserve: LiveData<T>
        get() {
            return liveData
        }

    protected fun handleError(e: Exception){
        Log.d(LOG_TAG, e.message.toString())
    }
}