package jt.projects.perfectday.push

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import jt.projects.model.DataModel
import jt.projects.perfectday.interactors.BirthdayFromPhoneInteractorImpl
import jt.projects.perfectday.interactors.GetFriendsFromVkUseCase
import jt.projects.perfectday.interactors.ScheduledEventInteractorImpl
import jt.projects.perfectday.presentation.MainActivity
import jt.projects.utils.LOG_TAG
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences
import jt.projects.utils.shared_preferences.VK_AUTH_TOKEN
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.getKoin
import java.time.LocalDate

class NotificationWorker(private val appContext: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(appContext, workerParameters) {

    companion object {
        const val TAG = "DailyReminder"
    }

    private var job: Job? = null

    private val scheduledEventInteractor by lazy { getKoin().get<ScheduledEventInteractorImpl>() }
    private val birthdayFromPhoneInteractor by lazy { getKoin().get<BirthdayFromPhoneInteractorImpl>() }
    private val getFriendsFromVkUseCase by lazy { getKoin().get<GetFriendsFromVkUseCase>() }

    override suspend fun doWork(): Result {
        job?.cancel()
        val currentDate = LocalDate.now()
        val vkToken: String? = getKoin().get<SimpleSettingsPreferences>().getSettings(VK_AUTH_TOKEN)


        job = CoroutineScope(Dispatchers.Main).launch {
            var notesCount = 0
            var birthdaysCount = 0

            //Ждём загрузку всех данных, чтобы пришли(и приводим к нужному типу)
            val friendsPhone = async { loadFriendsFromPhone(currentDate) }.await()
            val friendsVk = async { loadFriendsFromVK(vkToken, currentDate) }.await()


            async {
                scheduledEventInteractor
                    .getNotesByDate(currentDate)
                    .map {
                        notesCount = it.size
                        birthdaysCount = friendsPhone.size + friendsVk.size
                    }.onEach {
                        NotificationProvider(appContext).send(
                            "Сегодня",
                            "Дней рождения: $birthdaysCount, запланировано дел: ${notesCount}"
                        )
                    }
                    .collect()
            }
        }
        return Result.success()
    }

    private suspend fun loadFriendsFromPhone(date: LocalDate) = loadContent {
        birthdayFromPhoneInteractor.getFakeDataByDate(date)
    }

    private suspend fun loadFriendsFromVK(vkToken: String?, date: LocalDate) = loadContent {
        getFriendsFromVkUseCase.getFriendsByDate(
            vkToken, date
        )
    }

    private suspend fun loadContent(listener: suspend () -> List<DataModel>): List<DataModel> =
        try {
            listener.invoke()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(LOG_TAG, "$e")
            listOf()
        }
}
