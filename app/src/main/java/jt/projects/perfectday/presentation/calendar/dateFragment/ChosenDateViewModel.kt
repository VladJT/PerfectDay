package jt.projects.perfectday.presentation.calendar.dateFragment

import jt.projects.perfectday.core.AppDataCache
import jt.projects.perfectday.core.BaseViewModel
import jt.projects.utils.chosenCalendarDate
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences

class ChosenDateViewModel(
    settingsPreferences: SimpleSettingsPreferences,
    dataCache: AppDataCache
) : BaseViewModel(
    settingsPreferences,
    dataCache
) {

    override suspend fun loadBirthdaysFromPhone() {
        val dataPhone =
            dataCache.getBirthdaysFromPhoneByDate(chosenCalendarDate)
        if (dataPhone.isNotEmpty()) {
            addHeaderRow(PHONE_GROUP_LABEL)
            data.addAll(dataPhone)
        }
    }

    override suspend fun loadBirthdaysFromVk() {
        val dataVk = dataCache.getBirthdaysFromVkByDate(chosenCalendarDate)
        if (dataVk.isNotEmpty()) {
            addHeaderRow(VK_GROUP_LABEL)
            data.addAll(dataVk)
        }
    }

    override suspend fun loadScheduledEvents() {
        val scheduledEvents = dataCache.getScheduledEventsByDate(chosenCalendarDate)
        if (scheduledEvents.isNotEmpty()) {
            addHeaderRow(SCHEDULED_EVENT_GROUP_LABEL)
            data.addAll(scheduledEvents)
        }
    }
}