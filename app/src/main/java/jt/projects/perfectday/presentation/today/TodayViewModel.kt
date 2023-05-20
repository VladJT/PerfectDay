package jt.projects.perfectday.presentation.today

import androidx.lifecycle.*
import jt.projects.model.DataModel
import jt.projects.model.FriendType
import jt.projects.perfectday.core.extensions.*
import jt.projects.perfectday.interactors.*
import jt.projects.perfectday.presentation.today.adapter.birth.FriendItem
import jt.projects.perfectday.presentation.today.adapter.note.NoteItem
import jt.projects.utils.FACTS_COUNT
import jt.projects.utils.extensions.emptyString
import jt.projects.utils.shared_preferences.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.time.LocalDate

class TodayViewModel(
    private val settingsPreferences: SimpleSettingsPreferences,
    private val birthdayFromPhoneInteractor: BirthdayFromPhoneInteractorImpl,
    private val simpleNoticeInteractorImpl: SimpleNoticeInteractorImpl,
    private val holidayInteractor: HolidayInteractorImpl,
    private val getFriendsFromVkUseCase: GetFriendsFromVkUseCase,
    private val scheduledEventInteractorImpl: ScheduledEventInteractorImpl
) : ViewModel() {
    private val currentDate = LocalDate.now()
    private val datePeriod = currentDate.plusMonths(5)

    private val _holidayFlow = MutableStateFlow(DataModel.Holiday.EMPTY)
    val holidayFlow = _holidayFlow.asStateFlow()

    private val _friendsFlow = MutableStateFlow<List<FriendItem>>(listOf())
    val friendsFlow = _friendsFlow.asStateFlow()

    private val _factOfTheDayFlow = MutableStateFlow(DataModel.SimpleNotice.EMPTY)
    val factOfTheDayFlow = _factOfTheDayFlow.asStateFlow()

    private val _notesFlow = MutableStateFlow<List<NoteItem>>(listOf())
    val notesFlow get() = _notesFlow.asStateFlow()

    private val _noteIdFlow = createMutableSingleEventFlow<Int>()
    val noteIdFlow get() = _noteIdFlow.asSharedFlow()

    private var job: Job? = null
    private var progressMotionLayout = 0f

    init {
        loadAllContent()
    }

    private fun loadAllContent() {
        job?.cancel()
        asyncOrReturnEmptyList {
            val holiday = holidayInteractor.getCalendarificHolidayByDate(currentDate)
            _holidayFlow.tryEmit(holiday.firstOrNull() ?: DataModel.Holiday.CURRENT_DATE)
            holiday
        }
        asyncOrReturnEmptyList {
            val friends = getAllFriends()
            _friendsFlow.tryEmit(friends)
            friends
        }
        asyncOrReturnEmptyList {
            val fact = simpleNoticeInteractorImpl.getFactsByDate(currentDate, FACTS_COUNT)
            _factOfTheDayFlow.tryEmit(fact.firstOrNull() ?: DataModel.SimpleNotice.EMPTY)
            fact
        }
        job = viewModelScope.launch {
            scheduledEventInteractorImpl.getNotesByDate(currentDate)
                .map { it.map(::mapToNoteItem) }
                .onEach(_notesFlow::tryEmit)
                .collect()
        }
    }

    private suspend fun getAllFriends(): List<FriendItem> {
        val vkToken: String? = settingsPreferences.getStringOrEmptyString(VK_AUTH_TOKEN)
        val phonesFriend =
            birthdayFromPhoneInteractor.getFriendsByPeriod(currentDate, datePeriod).take(5)
        val vkFriends =
            getFriendsFromVkUseCase.getFriendsByPeriodDate(vkToken, currentDate, datePeriod).take(5)
        return phonesFriend
            .map(::toFriendItem)
            .toMutableList()
            .apply { addAll(vkFriends.map(::toFriendItem)) }
    }

    private fun toFriendItem(data: DataModel.BirthdayFromPhone): FriendItem = data.run {
        FriendItem(
            id = id,
            type = FriendType.PHONE,
            name = name,
            birthDate = birthDate,
            age = age ?: 0,
            photoUrl = photoUri ?: emptyString()
        )
    }

    private fun toFriendItem(data: DataModel.BirthdayFromVk): FriendItem = data.run {
        FriendItem(
            id = vkId.toString(),
            type = FriendType.VK,
            name = name,
            birthDate = birthDate,
            age = age,
            photoUrl = photoUrl
        )
    }

    private fun mapToNoteItem(data: DataModel.ScheduledEvent): NoteItem = data.run {
        NoteItem(
            id = id,
            name = name,
            date = date,
            description = description
        )
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

    fun getProgressMotion(): Float = progressMotionLayout

    fun setProgress(progressMotionLayout: Float) {
        this.progressMotionLayout = progressMotionLayout
    }
}