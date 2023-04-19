package jt.projects.perfectday.presentation.reminder

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jt.projects.model.DataModel
import jt.projects.perfectday.core.extensions.createMutableSingleEventFlow
import jt.projects.perfectday.core.viewholders.PhoneBookProvider
import jt.projects.perfectday.interactors.BirthdayFromPhoneInteractorImpl
import jt.projects.perfectday.interactors.GetFriendsFromVkUseCase
import jt.projects.perfectday.interactors.ScheduledEventInteractorImpl
import jt.projects.utils.LOG_TAG
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
import java.time.LocalDate

abstract class BaseChildViewModel(
    protected val settingsPreferences: SimpleSettingsPreferences,
    private val birthdayFromPhoneInteractor: BirthdayFromPhoneInteractorImpl,
    private val getFriendsFromVkUseCase: GetFriendsFromVkUseCase,
    private val scheduledEventInteractor: ScheduledEventInteractorImpl,
    private val phoneBookProvider: PhoneBookProvider
) :
    ViewModel() {

    companion object {
        const val PHONE_GROUP_LABEL = "Дни рождения контактов телефона"
        const val VK_GROUP_LABEL = "Дни рождения друзей ВКонтакте"
        const val SCHEDULED_EVENT_GROUP_LABEL = "Запланированные события"
    }

    abstract fun getStartDate(): LocalDate
    abstract fun getEndDate(): LocalDate

    private val _resultRecycler = MutableStateFlow<List<DataModel>>(listOf())
    val resultRecycler get() = _resultRecycler.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading get() = _isLoading.asStateFlow()

    private val _noteFlow = createMutableSingleEventFlow<DataModel.ScheduledEvent>()
    val noteFlow get() = _noteFlow.asSharedFlow()

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
                loadContent {
                    birthdayFromPhoneInteractor.getContactsInInterval(
                        getStartDate(),
                        getEndDate()
                    )
                }
            }
            val loadVkFriends = async {
                loadContent {
                    getFriendsFromVkUseCase.getFriendsByPeriodDate(
                        vkToken,
                        getStartDate(),
                        getEndDate()
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
            scheduledEventInteractor.getScheduledEventsByPeriod(getStartDate(), getEndDate())
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
    }

    fun onEditNoteClicked(dataModel: DataModel) {
        if (dataModel is DataModel.ScheduledEvent) _noteFlow.tryEmit(dataModel)
    }

    fun onItemClicked(data: DataModel) {
        if (data is DataModel.BirthdayFromPhone) {
            phoneBookProvider.openContact(data)
        }
    }

    fun onSwipeToRefreshMove(): Unit = loadAllContent()
}