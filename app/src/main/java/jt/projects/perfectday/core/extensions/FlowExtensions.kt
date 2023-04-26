package jt.projects.perfectday.core.extensions

import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

fun <T> createMutableSingleEventFlow(): MutableSharedFlow<T> =
    MutableSharedFlow(0, 1, BufferOverflow.DROP_OLDEST)

fun ViewModel.launchOrError(
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    action: suspend () -> Unit,
    error: (Exception) -> Unit
) {
    viewModelScope.launch(dispatcher) {
        try {
            action.invoke()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            error.invoke(e)
        }
    }
}