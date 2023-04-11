package jt.projects.perfectday.presentation.today

import jt.projects.model.AppState
import jt.projects.perfectday.core.BaseViewModel
import jt.projects.perfectday.interactors.*
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import java.time.LocalDate
import java.time.Month

class TodayViewModel(
    settingsPreferences: SimpleSettingsPreferences,
    birthdayFromPhoneInteractor: BirthdayFromPhoneInteractorImpl,
    simpleNoticeInteractorImpl: SimpleNoticeInteractorImpl,
    getFriendsFromVkUseCase: GetFriendsFromVkUseCase,
    scheduledEventInteractorImpl: ScheduledEventInteractorImpl,
    holidayInteractorImpl: HolidayInteractorImpl
) :
    BaseViewModel(
        settingsPreferences,
        birthdayFromPhoneInteractor,
        simpleNoticeInteractorImpl,
        getFriendsFromVkUseCase,
        scheduledEventInteractorImpl,
        holidayInteractorImpl
    ) {
    private val currentDate = LocalDate.now()


    override suspend fun loadBirthdaysFromPhone() {
        //    val dataByDate = birthdayFromPhoneInteractor.getDataByDate(currentDate)
        //    data.addAll(dataByDate)
    }

    override suspend fun loadBirthdaysFromVk() {
        //Пока не добавил в адаптер
        //     val friendsFromVk = loadFriendsFromVk()
        //     Log.d("TAG", "friendsVk= $friendsFromVk")
    }

    override suspend fun loadInterestingFacts() {
//        val factsByDate =
//            simpleNoticeInteractorImpl.getFactsByDate(currentDate, FACTS_COUNT)
//        data.addAll(factsByDate)
    }

    override suspend fun loadHolidays() {
        throw Exception("some error")
//        val holidayByDate = holidayInteractorImpl.getHolidayByDate(LocalDate.of(2023,Month.MAY,9), HOLIDAY_COUNTRY)
       // val holidayByDate = holidayInteractorImpl.getFakeHoliday()
       // data.addAll(holidayByDate)
    }

    override suspend fun loadScheduledEvents() {
        //  val scheduledEvents = scheduledEventInteractorImpl.getScheduledEventsByDate(currentDate)
        //  data.addAll(scheduledEvents)


        val scheduledEvents = scheduledEventInteractorImpl.getScheduledEventsByDate(currentDate)
        data.addAll(scheduledEvents.toList())

        scheduledEvents.collect{
            println(it)
        }

    }

}