package jt.projects.perfectday.core

import jt.projects.model.DataModel
import jt.projects.perfectday.interactors.*
import jt.projects.utils.isPeriodBirthdayDate
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences
import jt.projects.utils.shared_preferences.VK_AUTH_TOKEN
import jt.projects.utils.sortComparatorByMonthAndDay
import java.time.LocalDate

class AppDataCache(
    private val settingsPreferences: SimpleSettingsPreferences,
    private val simpleNoticeInteractor: SimpleNoticeInteractorImpl,
    private val holidayInteractor: HolidayInteractorImpl,
    private val birthdayFromPhoneInteractor: BirthdayFromPhoneInteractorImpl,
    private val getFriendsFromVkUseCase: GetFriendsFromVkUseCase,
    private val scheduledEventInteractor: ScheduledEventInteractorImpl
) {
    private val vkToken: String? by lazy { settingsPreferences.getSettings(VK_AUTH_TOKEN) }

    private var birthdaysFromPhone: List<DataModel.BirthdayFromPhone>? = null
    private var birthdaysFromVk: List<DataModel.BirthdayFromVk>? = null
    private var scheduledEvents: List<DataModel.ScheduledEvent>? = null
    private var facts: List<DataModel.SimpleNotice>? = null
    private var holidays: List<DataModel.Holiday>? = null

    /**
     * Phone Data
     */
    suspend fun getAllBirthdaysFromPhone(): List<DataModel.BirthdayFromPhone> {
        if (birthdaysFromPhone == null) {
            birthdaysFromPhone = birthdayFromPhoneInteractor.getContacts()
        }
        return birthdaysFromPhone as List<DataModel.BirthdayFromPhone>
    }

    suspend fun getBirthdaysFromPhoneByPeriod(
        startDate: LocalDate,
        endDate: LocalDate
    ): List<DataModel.BirthdayFromPhone> =
        getAllBirthdaysFromPhone()
            .filter { isPeriodBirthdayDate(startDate, endDate, it.birthDate) }
            .sortedWith(sortComparatorByMonthAndDay)


    suspend fun getBirthdaysFromPhoneByDate(
        date: LocalDate
    ): List<DataModel.BirthdayFromPhone> =
        getBirthdaysFromPhoneByPeriod(date, date)


    /**
     * VK Data
     */
    suspend fun getAllBirthdaysFromVk(): List<DataModel.BirthdayFromVk> {
        if (birthdaysFromVk == null) {
            birthdaysFromVk = getFriendsFromVkUseCase.getAllFriends(vkToken)
        }
        return birthdaysFromVk as List<DataModel.BirthdayFromVk>
    }

    suspend fun getBirthdaysFromVkByPeriod(
        startDate: LocalDate,
        endDate: LocalDate
    ): List<DataModel.BirthdayFromVk> =
        getAllBirthdaysFromVk()
            .filter { isPeriodBirthdayDate(startDate, endDate, it.birthDate) }
            .sortedWith(sortComparatorByMonthAndDay)

    suspend fun getBirthdaysFromVkByDate(
        date: LocalDate
    ): List<DataModel.BirthdayFromVk> =
        getBirthdaysFromVkByPeriod(date, date)


    /**
     * ScheduledEvents
     */
    suspend fun getAllScheduledEvents(): List<DataModel.ScheduledEvent> {
        if (scheduledEvents == null) {
            scheduledEvents = scheduledEventInteractor.getAll()
        }
        return scheduledEvents as List<DataModel.ScheduledEvent>
    }

    suspend fun getScheduledEventsByPeriod(
        startDate: LocalDate,
        endDate: LocalDate
    ): List<DataModel.ScheduledEvent> =
        getAllScheduledEvents()
            .filter { it.date in startDate..endDate }
            .sortedBy { it.date }

    suspend fun getScheduledEventsByDate(
        date: LocalDate
    ): List<DataModel.ScheduledEvent> =
        getScheduledEventsByPeriod(date, date)

    suspend fun deleteScheduledEventById(id: Int) {
        scheduledEventInteractor.deleteScheduledEventById(id)
    }

    fun cleanScheduledEventsCache(){
        scheduledEvents = null
    }

}