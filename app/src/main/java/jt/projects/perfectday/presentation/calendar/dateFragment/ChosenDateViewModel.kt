package jt.projects.perfectday.presentation.calendar.dateFragment

import jt.projects.perfectday.core.BaseViewModel
import jt.projects.perfectday.interactors.BirthdayFromPhoneInteractorImpl
import jt.projects.perfectday.interactors.GetFriendsFromVkUseCase
import jt.projects.perfectday.interactors.ScheduledEventInteractorImpl
import jt.projects.utils.chosenCalendarDate
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences
import jt.projects.utils.shared_preferences.VK_AUTH_TOKEN

class ChosenDateViewModel(
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
        val dataPhone =
            birthdayFromPhoneInteractor.getContactsByDay(chosenCalendarDate)
        if (dataPhone.isNotEmpty()) {
            addHeaderRow(PHONE_GROUP_LABEL)
            data.addAll(dataPhone)
        }
    }

    override suspend fun loadBirthdaysFromVk() {
        val dataVk = getFriendsFromVkUseCase.getFriendsByDate(
            settingsPreferences.getSettings(
                VK_AUTH_TOKEN
            ), chosenCalendarDate
        )
        if (dataVk.isNotEmpty()) {
            addHeaderRow(VK_GROUP_LABEL)
            data.addAll(dataVk)
        }
    }

    override suspend fun loadScheduledEvents() {
        val scheduledEvents = scheduledEventInteractor.getScheduledEventsByDate(chosenCalendarDate)
        if (scheduledEvents.isNotEmpty()) {
            addHeaderRow(SCHEDULED_EVENT_GROUP_LABEL)
            data.addAll(scheduledEvents)
        }
    }
}