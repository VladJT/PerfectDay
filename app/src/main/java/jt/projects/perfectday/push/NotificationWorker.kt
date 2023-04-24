package jt.projects.perfectday.push

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import jt.projects.model.DataModel
import jt.projects.perfectday.interactors.ScheduledEventInteractorImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.getKoin
import java.time.LocalDate

class NotificationWorker(private val appContext: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(appContext, workerParameters) {

    companion object {
        const val TAG = "DailyReminder"
    }


    override suspend fun doWork(): Result {
        val date = LocalDate.now()
        val notesRepo = getKoin().get<ScheduledEventInteractorImpl>()

        val sf = MutableStateFlow<List<Int>>(listOf())

        CoroutineScope(// Scope - скоуп в котором будут существовать корутины
            Dispatchers.Main // механизм управления потоками
        ).launch {

            var size = 0
            val notes = async { notesRepo
                .getNotesByDate(date)
                .map {
                    size = it.size
                }.onEach {
                    sf.tryEmit(listOf(size))
                }
                .collect()
            }
            val c = notes.await()


            NotificationProvider(appContext).send("2", "New notes: ${c}")
        }

        return Result.success()
    }
}
