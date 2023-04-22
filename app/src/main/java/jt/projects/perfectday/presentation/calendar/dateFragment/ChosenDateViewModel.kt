package jt.projects.perfectday.presentation.calendar.dateFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jt.projects.model.DataModel
import jt.projects.perfectday.core.PhoneBookProvider
import jt.projects.perfectday.core.extensions.createMutableSingleEventFlow
import jt.projects.perfectday.interactors.BirthdayFromPhoneInteractorImpl
import jt.projects.perfectday.interactors.GetFriendsFromVkUseCase
import jt.projects.perfectday.interactors.ScheduledEventInteractorImpl
import jt.projects.utils.LOG_TAG
import jt.projects.utils.PHONE_GROUP_LABEL
import jt.projects.utils.SCHEDULED_EVENT_GROUP_LABEL
import jt.projects.utils.VK_GROUP_LABEL
import jt.projects.utils.chosenCalendarDate
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences
import jt.projects.utils.shared_preferences.VK_AUTH_TOKEN
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ChosenDateViewModel(
    private val settingsPreferences: SimpleSettingsPreferences,
    private val birthdayFromPhoneInteractor: BirthdayFromPhoneInteractorImpl,
    private val getFriendsFromVkUseCase: GetFriendsFromVkUseCase,
    private val scheduledEventInteractor: ScheduledEventInteractorImpl,
    private val phoneBookProvider: PhoneBookProvider
) :
    ViewModel() {

    private val statusMessage = MutableLiveData<Event<String>>()

    val message : LiveData<Event<String>>
        get() = statusMessage

    private val _resultRecycler = MutableStateFlow<List<DataModel>>(listOf())
    val resultRecycler get() = _resultRecycler.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading get() = _isLoading.asStateFlow()

    private val _noteIdFlow = createMutableSingleEventFlow<Int>()
    val noteIdFlow get() = _noteIdFlow.asSharedFlow()

    private var job: Job? = null

    init {
        loadAllContent()
    }

    private fun loadAllContent() {
        job?.cancel()
        val vkToken: String? = settingsPreferences.getSettings(VK_AUTH_TOKEN)

        _isLoading.tryEmit(true)

        job = viewModelScope.launch {
            //Запускаем параллельно загрузку данных
            val loadPhoneFriends = async {
                loadContent { birthdayFromPhoneInteractor.getContactsByDay(chosenCalendarDate) }
            }
            val loadVkFriends = async {
                loadContent {
                    getFriendsFromVkUseCase.getFriendsByDate(
                        vkToken,
                        chosenCalendarDate
                    )
                }
            }


            //Ждём загрузку всех данных, чтобы пришли(и приводим к нужному типу)
            val friendPhone =
                loadPhoneFriends.await().filterIsInstance<DataModel.BirthdayFromPhone>()
            val friendsVk = loadVkFriends.await().filterIsInstance<DataModel.BirthdayFromVk>()


            /*Подписываемся на изменения базы данных(а именно заметок на текущий день)
            * и каждый раз когда база изменяется, создаем items для RecyclerView
            * с нужными данными, после чего emit во фрагмент*/
            scheduledEventInteractor.getNotesByDate(chosenCalendarDate)
                .map {
                    val items = mutableListOf<DataModel>()
                    items.apply {
                        addAll(it.withHeader(SCHEDULED_EVENT_GROUP_LABEL))
                        addAll(friendPhone.withHeader(PHONE_GROUP_LABEL))
                        addAll(friendsVk.withHeader(VK_GROUP_LABEL))
                    }
                }
                .onEach {
                    _resultRecycler.tryEmit(it)
                    _isLoading.tryEmit(false)
                }
                .collect()
        }
    }

    private fun List<DataModel>.withHeader(headerName: String): List<DataModel> {
        if (this.isEmpty()) {
            return this
        }
        val data = mutableListOf<DataModel>(DataModel.SimpleNotice(headerName, ""))
        data.addAll(this)
        return data
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

    fun onDeleteNoteClicked(id: Int) {
        viewModelScope.launch {
            scheduledEventInteractor.deleteScheduledEventById(id)
        }
        statusMessage.value = Event("dismiss")
    }

    fun onEditNoteClicked(noteId: Int) {
        _noteIdFlow.tryEmit(noteId)
        statusMessage.value = Event("dismiss")
    }

    fun onItemClicked(data: DataModel) {
        fun onItemClicked(data: DataModel) {
            if (data is DataModel.BirthdayFromPhone) {
                phoneBookProvider.openContact(data)
            }
        }
    }

    fun onSwipeToRefreshMove(): Unit = loadAllContent()
}