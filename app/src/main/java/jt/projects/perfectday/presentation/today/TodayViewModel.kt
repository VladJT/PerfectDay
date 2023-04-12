package jt.projects.perfectday.presentation.today

import android.util.Log
import androidx.lifecycle.*
import jt.projects.model.DataModel
import jt.projects.perfectday.interactors.*
import jt.projects.perfectday.presentation.today.adapter.TodayItem
import jt.projects.utils.FACTS_COUNT
import jt.projects.utils.shared_preferences.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.time.LocalDate

private const val TAG = "TodayViewModel"

class TodayViewModel(
    settingsPreferences: SimpleSettingsPreferences,
    birthdayFromPhoneInteractor: BirthdayFromPhoneInteractorImpl,
    simpleNoticeInteractorImpl: SimpleNoticeInteractorImpl,
    private val getFriendsFromVkUseCase: GetFriendsFromVkUseCase,
    private val scheduledEventInteractorImpl: ScheduledEventInteractorImpl
) : ViewModel() {
    private val currentDate = LocalDate.now()
    private val vkToken: String? by lazy { settingsPreferences.getSettings(VK_AUTH_TOKEN) }

    private val _resultRecycler = MutableStateFlow<List<TodayItem>>(listOf())
    val resultRecycler get() = _resultRecycler.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading get() = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            //Запускаем параллельно загрузку данных
            val loadPhoneFriends = async { loadContent(birthdayFromPhoneInteractor::getContacts) }
            val loadVkFriends = async { loadFriendsFromVk() }
            val loadFacts = async {
                loadContent {
                    simpleNoticeInteractorImpl.getFactsByDate(currentDate, FACTS_COUNT)
                }
            }

            //Ждём загрузку всех данных, чтобы пришли(и приводим к нужному типу)
            val friendsVk = loadVkFriends.await().filterIsInstance<DataModel.BirthdayFromVk>()
            val friendsPhone = loadPhoneFriends.await().filterIsInstance<DataModel.BirthdayFromPhone>()
            val facts = loadFacts.await().filterIsInstance<DataModel.SimpleNotice>()

            /*Подписываемся на изменения базы данных(а именно заметок на текущий день)
            * и каждый раз когда база изменяется, создаем items для RecyclerView
            * с нужными данными, после чего emit во фрагмент*/
            scheduledEventInteractorImpl.getNotesByDate(currentDate)
                .map {
                    val notes = it.map { note -> TodayItem.Notes(note) }

                    val items = mutableListOf<TodayItem>()
                    items.apply {
                        add(TodayItem.Holiday(listOf()))
                        add(TodayItem.Friends(friendsVk, friendsPhone))
                        add(TodayItem.FactOfDay(facts))
                        addAll(notes)
                    }
                }
                .onEach {
                    _resultRecycler.tryEmit(it)
                    _isLoading.tryEmit(false)
                }
                .collect()
        }
    }

    private suspend fun loadFriendsFromVk(): List<DataModel> {
        if (vkToken == null || vkToken!!.isEmpty()) return emptyList()
        return loadContent {
            getFriendsFromVkUseCase.getFriends(vkToken!!)
        }
    }

    private suspend fun loadContent(listener: suspend () -> List<DataModel>): List<DataModel> =
        try {
            listener.invoke()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "$e")
            listOf()
        }

    fun onDeleteNoteClicked(id: Int) {
        viewModelScope.launch {
            scheduledEventInteractorImpl.deleteScheduledEventById(id)
        }
    }
}