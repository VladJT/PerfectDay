package jt.projects.perfectday.presentation.calendar.dateFragment

import jt.projects.perfectday.core.BaseViewModel
import jt.projects.perfectday.interactors.BirthdayFromPhoneInteractorImpl
import jt.projects.perfectday.interactors.GetFriendsFromVkUseCase
import jt.projects.perfectday.interactors.ScheduledEventInteractorImpl
import jt.projects.perfectday.interactors.SimpleNoticeInteractorImpl
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences

class ChosenDateViewModel(
    settingsPreferences: SimpleSettingsPreferences,
    birthdayFromPhoneInteractor: BirthdayFromPhoneInteractorImpl,
    simpleNoticeInteractorImpl: SimpleNoticeInteractorImpl,
    getFriendsFromVkUseCase: GetFriendsFromVkUseCase,
    scheduledEventInteractorImpl: ScheduledEventInteractorImpl
) : BaseViewModel(
    settingsPreferences,
    birthdayFromPhoneInteractor,
    simpleNoticeInteractorImpl,
    getFriendsFromVkUseCase,
    scheduledEventInteractorImpl
) {

    override suspend fun loadHolidays() {
//        TODO("Not yet implemented")
    }

    override suspend fun loadInterestingFacts() {
//        TODO("Not yet implemented")
    }

    override suspend fun loadBirthdaysFromPhone() {
        val allData = birthdayFromPhoneInteractor.getAllData()
        data.addAll(allData)
    }

    override suspend fun loadBirthdaysFromVk() {
//        TODO("Not yet implemented")
    }

    override suspend fun loadScheduledEvents() {
//        TODO("Not yet implemented")
    }
}