package jt.projects.perfectday.presentation.reminder

import jt.projects.perfectday.core.BaseViewModel
import jt.projects.perfectday.interactors.BirthdayFromPhoneInteractorImpl
import jt.projects.perfectday.interactors.GetFriendsFromVkUseCase
import jt.projects.perfectday.interactors.ScheduledEventInteractorImpl
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences
import java.time.LocalDate

class ReminderViewModel(
    settingsPreferences: SimpleSettingsPreferences,
    birthdayFromPhoneInteractor: BirthdayFromPhoneInteractorImpl,
    getFriendsFromVkUseCase: GetFriendsFromVkUseCase,
    scheduledEventInteractorImpl: ScheduledEventInteractorImpl
) :
    BaseViewModel(
        settingsPreferences,
        birthdayFromPhoneInteractor,
        getFriendsFromVkUseCase,
        scheduledEventInteractorImpl
    ) {

    var isShowTomorrow = true

    private fun getStartDate(): LocalDate {
        return if (isShowTomorrow) LocalDate.now().plusDays(1)
        else LocalDate.now()
    }

    private fun getEndDate(): LocalDate {
        return if (isShowTomorrow) getStartDate()
        else getStartDate().plusDays(settingsPreferences.getDaysPeriodForReminderFragment())
    }


    override suspend fun loadBirthdaysFromPhone() {
        val dataByDate = birthdayFromPhoneInteractor.getContacts()
        data.addAll(dataByDate)
    }

    override suspend fun loadBirthdaysFromVk() {
        val vkFriends = loadFriendsFromVk()
        data.addAll(vkFriends)

    }

    override suspend fun loadScheduledEvents() {
        val scheduledEvents =
            scheduledEventInteractorImpl.getScheduledEventsByPeriod(getStartDate(), getEndDate())
        data.addAll(scheduledEvents)
    }

}