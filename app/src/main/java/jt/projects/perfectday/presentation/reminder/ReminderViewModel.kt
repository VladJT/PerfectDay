package jt.projects.perfectday.presentation.reminder

import jt.projects.perfectday.core.BaseViewModel
import jt.projects.perfectday.interactors.BirthdayFromPhoneInteractorImpl
import jt.projects.perfectday.interactors.GetFriendsFromVkUseCase
import jt.projects.perfectday.interactors.ScheduledEventInteractorImpl
import jt.projects.perfectday.interactors.SimpleNoticeInteractorImpl
import jt.projects.utils.REMINDER_PERIOD_DEFAULT
import jt.projects.utils.REMINDER_PERIOD_KEY
import jt.projects.utils.extensions.emptyString
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences
import java.time.LocalDate

class ReminderViewModel(
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


    private var startDate = LocalDate.now().plusDays(1)
    private var endDate = startDate

    fun setDatesToShowTomorrow() {
        startDate = LocalDate.now().plusDays(1)
        endDate = startDate
    }

    fun setDatesToShowLongPeriod() {
        startDate = LocalDate.now()
        endDate = startDate.plusDays(getPeriod())
    }

    fun getPeriod(): Long {
        val reminderPeriod = settingsPreferences.getSettings(REMINDER_PERIOD_KEY)
        return if (reminderPeriod.equals(emptyString())) REMINDER_PERIOD_DEFAULT
        else reminderPeriod!!.toLong()
    }

    override suspend fun loadBirthdaysFromPhone() {
        val dataByDate = birthdayFromPhoneInteractor.getDataByDate(startDate)
        data.addAll(dataByDate)
    }

    override suspend fun loadBirthdaysFromVk() {

    }

    override suspend fun loadInterestingFacts() {
        // на этом фрагменте нет данных
    }

    override suspend fun loadHolidays() {
        // на этом фрагменте нет данных
    }

    override suspend fun loadScheduledEvents() {
        val scheduledEvents = scheduledEventInteractorImpl.getScheduledEventsByPeriod(startDate, endDate)
        data.addAll(scheduledEvents)
    }

}