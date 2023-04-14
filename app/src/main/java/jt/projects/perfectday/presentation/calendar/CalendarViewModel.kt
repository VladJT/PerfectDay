package jt.projects.perfectday.presentation.calendar

import jt.projects.perfectday.core.BaseViewModel
import jt.projects.perfectday.interactors.BirthdayFromPhoneInteractorImpl
import jt.projects.perfectday.interactors.GetFriendsFromVkUseCase
import jt.projects.perfectday.interactors.ScheduledEventInteractorImpl
import jt.projects.perfectday.interactors.SimpleNoticeInteractorImpl
import jt.projects.repository.network.vk.VkNetworkRepository
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences
import jt.projects.utils.shared_preferences.VK_AUTH_TOKEN

class CalendarViewModel(
    settingsPreferences: SimpleSettingsPreferences,
    birthdayFromPhoneInteractor: BirthdayFromPhoneInteractorImpl,
    getFriendsFromVkUseCase: GetFriendsFromVkUseCase,
    scheduledEventInteractorImpl: ScheduledEventInteractorImpl
) : BaseViewModel(
    settingsPreferences,
    birthdayFromPhoneInteractor,
    getFriendsFromVkUseCase,
    scheduledEventInteractorImpl
) {


    override suspend fun loadBirthdaysFromPhone() {
        val allData = birthdayFromPhoneInteractor.getContacts()
        data.addAll(allData)
    }

    override suspend fun loadBirthdaysFromVk() {
        val token = settingsPreferences.getSettings(VK_AUTH_TOKEN)
        val allData = getFriendsFromVkUseCase.getAllFriends(token)
        data.addAll(allData)
    }

    override suspend fun loadScheduledEvents() {
        val allData = scheduledEventInteractorImpl.getAll()
        data.addAll(allData)
    }
}