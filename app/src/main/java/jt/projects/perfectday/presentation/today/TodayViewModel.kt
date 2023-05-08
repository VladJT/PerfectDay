package jt.projects.perfectday.presentation.today

import androidx.lifecycle.*
import jt.projects.model.DataModel
import jt.projects.perfectday.core.extensions.*
import jt.projects.perfectday.interactors.*
import jt.projects.perfectday.presentation.today.adapter.main.TodayItem
import jt.projects.utils.FACTS_COUNT
import jt.projects.utils.shared_preferences.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.time.LocalDate

class TodayViewModel(
    settingsPreferences: SimpleSettingsPreferences,
    private val birthdayFromPhoneInteractor: BirthdayFromPhoneInteractorImpl,
    private val simpleNoticeInteractorImpl: SimpleNoticeInteractorImpl,
    private val holidayInteractor: HolidayInteractorImpl,
    private val getFriendsFromVkUseCase: GetFriendsFromVkUseCase,
    private val scheduledEventInteractorImpl: ScheduledEventInteractorImpl
) : ViewModel() {
    private val currentDate = LocalDate.now()
    private val datePeriod = currentDate.plusMonths(5)

    private val _resultRecycler = MutableStateFlow<List<TodayItem>>(listOf())
    val resultRecycler get() = _resultRecycler.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading get() = _isLoading.asStateFlow()

    private val _noteIdFlow = createMutableSingleEventFlow<Int>()
    val noteIdFlow get() = _noteIdFlow.asSharedFlow()

    private var job: Job? = null
    private val vkToken: String? = settingsPreferences.getStringOrEmptyString(VK_AUTH_TOKEN)

    init {
        loadAllContent()
    }

    private fun loadAllContent() {
        job?.cancel()
        _isLoading.tryEmit(true)
        job = viewModelScope.launch {
            val holidays =
                asyncOrReturnEmptyList { holidayInteractor.getCalendarificHolidayByDate(currentDate) }
            val facts =
                asyncOrReturnEmptyList { simpleNoticeInteractorImpl.getFactsByDate(currentDate, FACTS_COUNT) }
            val friends = getAllFriends()
            /*Подписываемся на изменения базы данных(а именно заметок на текущий день)
            * и каждый раз когда база изменяется, создаем items для RecyclerView
            * с нужными данными, после чего emit во фрагмент*/
            scheduledEventInteractorImpl.getNotesByDate(currentDate)
                .map { mapToTodayItems(holidays, facts, friends, it) }
                .onEach {
                    _resultRecycler.tryEmit(it)
                    _isLoading.tryEmit(false)
                }
                .collect()
        }
    }

    private suspend fun getAllFriends(): List<DataModel.Friend> {
        val contactsFriends = asyncOrReturnEmptyList {
            birthdayFromPhoneInteractor.getFriendsByPeriod(currentDate, datePeriod).take(5)
        }
        val vkFriends = asyncOrReturnEmptyList {
            getFriendsFromVkUseCase.getFriendsByPeriodDate(vkToken, currentDate, datePeriod).take(5)
        }
        return contactsFriends
            .toMutableList()
            .apply { addAll(vkFriends) }
    }

    private fun mapToTodayItems(
        holidays: List<DataModel.Holiday>,
        facts: List<DataModel.SimpleNotice>,
        friends: List<DataModel.Friend>,
        notes: List<DataModel.ScheduledEvent>
    ): List<TodayItem> {
        val items = mutableListOf<TodayItem>()
        notes.map {  }
        return items.apply {
            add(TodayItem.Holiday(holidays))
            add(TodayItem.Friends(friends))
            add(TodayItem.FactOfDay(facts))
            addAll(notes.map { note -> TodayItem.Notes(note) })
        }
    }

    fun onDeleteNoteClicked(id: Int) {
        viewModelScope.launch {
            scheduledEventInteractorImpl.deleteScheduledEventById(id)
        }
    }

    fun onEditNoteClicked(noteId: Int) {
        _noteIdFlow.tryEmit(noteId)
    }

    fun onSwipeToRefreshMove(): Unit = loadAllContent()
}