package jt.projects.perfectday.presentation.reminder

import jt.projects.perfectday.core.AppDataCache
import jt.projects.perfectday.core.BaseViewModel
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences
import java.time.LocalDate

class ReminderViewModel(
    settingsPreferences: SimpleSettingsPreferences,
    dataCache: AppDataCache
) :
    BaseViewModel(
        settingsPreferences,
        dataCache
    ) {

    var isShowTomorrow = true

    private fun getStartDate(): LocalDate {
//        return if (isShowTomorrow) LocalDate.now().plusDays(1)
//        else
        return LocalDate.now()
    }

    private fun getEndDate(): LocalDate {
        return if (isShowTomorrow) getStartDate().plusDays(1)
        else getStartDate().plusDays(settingsPreferences.getDaysPeriodForReminderFragment())
    }

    override suspend fun loadBirthdaysFromPhone() {
        val dataPhone = dataCache.getBirthdaysFromPhoneByPeriod(getStartDate(), getEndDate())
        if (dataPhone.isNotEmpty()) {
            addHeaderRow(PHONE_GROUP_LABEL)
            data.addAll(dataPhone)
        }
    }

    override suspend fun loadBirthdaysFromVk() {
        val dataVk = dataCache.getBirthdaysFromVkByPeriod(getStartDate(), getEndDate())
        if (dataVk.isNotEmpty()) {
            addHeaderRow(VK_GROUP_LABEL)
            data.addAll(dataVk)
        }
    }

    override suspend fun loadScheduledEvents() {
        val scheduledEvents = dataCache.getScheduledEventsByPeriod(getStartDate(), getEndDate())
        if (scheduledEvents.isNotEmpty()) {
            addHeaderRow(SCHEDULED_EVENT_GROUP_LABEL)
            data.addAll(scheduledEvents)
        }
    }
}