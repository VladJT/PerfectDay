package jt.projects.perfectday.presentation.today

import androidx.lifecycle.*
import jt.projects.model.*
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
    private var job: Job? = null
    private var progressMotionLayout = 0f
    private val loadingFriends = List(3) { FriendItem.LOADING }

    private val _holidayFlow = MutableStateFlow(DataModel.Holiday.EMPTY)
    val holidayFlow = _holidayFlow.asStateFlow()

    private val _friendsFlow = MutableStateFlow(loadingFriends)
    val friendsFlow = _friendsFlow.asStateFlow()

    private val _factOfTheDayFlow = MutableStateFlow(DataModel.SimpleNotice.EMPTY)
    val factOfTheDayFlow = _factOfTheDayFlow.asStateFlow()

    private val _notesFlow = MutableStateFlow<List<NoteItem>>(listOf())
    val notesFlow get() = _notesFlow.asStateFlow()

    private val _noteIdFlow = createMutableSingleEventFlow<Int>()
    val noteIdFlow get() = _noteIdFlow.asSharedFlow()

    init {
        loadAllContent()
    }

    private fun loadAllContent() {
        job?.cancel()
        _friendsFlow.tryEmit(loadingFriends)
        launchOrError(
            action = {
                val holiday = holidayInteractor.getCalendarificHolidayByDate(currentDate)
                _holidayFlow.tryEmit(holiday.firstOrNull() ?: DataModel.Holiday.CURRENT_DATE)
            },
            error = { _holidayFlow.tryEmit(DataModel.Holiday.CURRENT_DATE) }
        )
        launchOrError(
            action = {
                val friends = getAllFriends()
                delay(2000)
                _friendsFlow.tryEmit(friends)
            },
            error = { _friendsFlow.tryEmit(listOf(FriendItem.EMPTY)) }
        )
        launchOrError(
            action = {
                val fact = simpleNoticeInteractorImpl.getFactsByDate(currentDate, FACTS_COUNT)
                _factOfTheDayFlow.tryEmit(fact.firstOrNull() ?: DataModel.SimpleNotice.EMPTY)
            }) {}
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
            .apply {
                addAll(vkFriends.map(::toFriendItem))
                if (isEmpty()) add(FriendItem.EMPTY)
            }
    }

    private fun toFriendItem(data: DataModel.BirthdayFromPhone): FriendItem = data.run {
        FriendItem(
            id = id,
            type = FriendType.PHONE,
            name = name,
            birthDate = birthDate,
            age = age ?: 0,
            photoUrl = photoUri ?: emptyString(),
            isTodayBirth = isTodayBirthday(birthDate)
        )
    }

    private fun toFriendItem(data: DataModel.BirthdayFromVk): FriendItem = data.run {
        FriendItem(
            id = vkId.toString(),
            type = FriendType.VK,
            name = name,
            birthDate = birthDate,
            age = age,
            photoUrl = photoUrl,
            isTodayBirth = isTodayBirthday(birthDate)
        )
    }

    private fun isTodayBirthday(birthday: LocalDate): Boolean =
        currentDate.dayOfMonth == birthday.dayOfMonth && currentDate.monthValue == birthday.monthValue

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