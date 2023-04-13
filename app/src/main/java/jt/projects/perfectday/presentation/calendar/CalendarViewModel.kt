package jt.projects.perfectday.presentation.calendar

import jt.projects.perfectday.core.BaseViewModel
import jt.projects.perfectday.interactors.BirthdayFromPhoneInteractorImpl
import jt.projects.perfectday.interactors.GetFriendsFromVkUseCase
import jt.projects.perfectday.interactors.ScheduledEventInteractorImpl
import jt.projects.perfectday.interactors.SimpleNoticeInteractorImpl
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences

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
//        TODO("Not yet implemented")
    }

    override suspend fun loadScheduledEvents() {
//        TODO("Not yet implemented")
    }
}