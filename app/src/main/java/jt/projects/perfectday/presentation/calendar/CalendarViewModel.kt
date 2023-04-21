package jt.projects.perfectday.presentation.calendar

import jt.projects.perfectday.core.BaseViewModel
import jt.projects.perfectday.interactors.BirthdayFromPhoneInteractorImpl
import jt.projects.perfectday.interactors.GetFriendsFromVkUseCase
import jt.projects.perfectday.interactors.ScheduledEventInteractorImpl
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences
import jt.projects.utils.shared_preferences.VK_AUTH_TOKEN

class CalendarViewModel(
    settingsPreferences: SimpleSettingsPreferences,
    birthdayFromPhoneInteractor: BirthdayFromPhoneInteractorImpl,
    getFriendsFromVkUseCase: GetFriendsFromVkUseCase,
    scheduledEventInteractor: ScheduledEventInteractorImpl
) : BaseViewModel(
    settingsPreferences,
    birthdayFromPhoneInteractor,
    getFriendsFromVkUseCase,
    scheduledEventInteractor
) {


    override suspend fun loadBirthdaysFromPhone() {
        val allData = birthdayFromPhoneInteractor.getContacts()
        data.addAll(allData)
    }

    override suspend fun loadBirthdaysFromVk() {
        val allData = getFriendsFromVkUseCase.getAllFriends(
            settingsPreferences.getSettings(
                VK_AUTH_TOKEN
            )
        )
        data.addAll(allData)
    }

    override suspend fun loadScheduledEvents() {
        val allData = scheduledEventInteractor.getAll()
        data.addAll(allData)
    }
}