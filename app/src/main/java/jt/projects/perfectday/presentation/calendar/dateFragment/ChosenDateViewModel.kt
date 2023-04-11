package jt.projects.perfectday.presentation.calendar.dateFragment

import jt.projects.perfectday.core.BaseViewModel
import jt.projects.perfectday.interactors.*
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences

class ChosenDateViewModel(
    settingsPreferences: SimpleSettingsPreferences,
    birthdayFromPhoneInteractor: BirthdayFromPhoneInteractorImpl,
    simpleNoticeInteractorImpl: SimpleNoticeInteractorImpl,
    getFriendsFromVkUseCase: GetFriendsFromVkUseCase,
    scheduledEventInteractorImpl: ScheduledEventInteractorImpl,
    holidayInteractorImpl: HolidayInteractorImpl
) : BaseViewModel(
    settingsPreferences,
    birthdayFromPhoneInteractor,
    simpleNoticeInteractorImpl,
    getFriendsFromVkUseCase,
    scheduledEventInteractorImpl,
    holidayInteractorImpl
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