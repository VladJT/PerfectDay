package jt.projects.perfectday.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import jt.projects.model.AppState

//Создадим базовую ViewModel, куда вынесем общий для всех функционал
abstract class BaseViewModel<T : AppState> : ViewModel() {
    protected val liveData: MutableLiveData<T> = MutableLiveData()

    val liveDataForViewToObserve: LiveData<T>
        get() {
            return liveData
        }
}