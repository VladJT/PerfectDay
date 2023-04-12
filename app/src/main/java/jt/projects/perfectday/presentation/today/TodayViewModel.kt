package jt.projects.perfectday.presentation.today

import jt.projects.model.DataModel
import jt.projects.perfectday.core.BaseViewModel
import jt.projects.perfectday.interactors.BirthdayFromPhoneInteractorImpl
import jt.projects.perfectday.interactors.GetFriendsFromVkUseCase
import jt.projects.perfectday.interactors.ScheduledEventInteractorImpl
import jt.projects.perfectday.interactors.SimpleNoticeInteractorImpl
import jt.projects.utils.FACTS_COUNT
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

class TodayViewModel(
    settingsPreferences: SimpleSettingsPreferences,
    birthdayFromPhoneInteractor: BirthdayFromPhoneInteractorImpl,
    simpleNoticeInteractorImpl: SimpleNoticeInteractorImpl,
    getFriendsFromVkUseCase: GetFriendsFromVkUseCase,
    scheduledEventInteractorImpl: ScheduledEventInteractorImpl
) :
    BaseViewModel(
        settingsPreferences,
        birthdayFromPhoneInteractor,
        simpleNoticeInteractorImpl,
        getFriendsFromVkUseCase,
        scheduledEventInteractorImpl
    ) {
    private val currentDate = LocalDate.now()

    private val _friendsFlow = MutableStateFlow(listOf<DataModel.BirthdayFromVk>())
    val friendsFlow get() = _friendsFlow.asStateFlow()

    init {
        viewModelScope.launch {
            val friendsVk = loadFriendsFromVk()
            val friendsPhone = birthdayFromPhoneInteractor.getContacts()
            val facts = simpleNoticeInteractorImpl.getFakeFacts()


            scheduledEventInteractorImpl.getNotesByDate(currentDate)
                .map {
                    val items = mutableListOf<TodayItem>()
                    items.apply {
                        add(TodayItem.Friends(friendsVk, friendsPhone))
                        add(TodayItem.FactOfDay(facts))
                        add(TodayItem.Notes(it))
                    }
                }
                .onEach {
                    Log.d("TAG", "items $it")
                }
                .collect()
        }
    }

    override suspend fun loadBirthdaysFromPhone() {
        val dataByDate = birthdayFromPhoneInteractor.getDataByDate(currentDate)
        data.addAll(dataByDate)
    }

    override suspend fun loadBirthdaysFromVk() {
        //Пока не добавил в адаптер
        val friendsFromVk = loadFriendsFromVk()
        //крашит из за main view holder
//        data.addAll(friendsFromVk)
        _friendsFlow.tryEmit(friendsFromVk.filterIsInstance<DataModel.BirthdayFromVk>())
    }

    override suspend fun loadInterestingFacts() {
        val factsByDate =
            simpleNoticeInteractorImpl.getFactsByDate(currentDate, FACTS_COUNT)
        data.addAll(factsByDate)
    }

    override suspend fun loadHolidays() {
        throw Exception("some error")
    }

    override suspend fun loadScheduledEvents() {
        val scheduledEvents = scheduledEventInteractorImpl.getScheduledEventsByDate(currentDate)
        data.addAll(scheduledEvents)
    }

}