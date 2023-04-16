package jt.projects.perfectday.presentation.reminder

import jt.projects.model.DataModel
import jt.projects.perfectday.core.BaseViewModel
import jt.projects.perfectday.core.AppDataCache
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
        return if (isShowTomorrow) LocalDate.now().plusDays(1)
        else LocalDate.now()
    }

    private fun getEndDate(): LocalDate {
        return if (isShowTomorrow) getStartDate()
        else getStartDate().plusDays(settingsPreferences.getDaysPeriodForReminderFragment())
    }

    override suspend fun loadBirthdaysFromPhone() {
        val dataPhone = dataCache.getBirthdaysFromPhoneByPeriod(getStartDate(), getEndDate())
        if (dataPhone.isNotEmpty()) {
            addHeaderRow("Дни рождения контактов телефона")
            data.addAll(dataPhone)
        }
    }

    override suspend fun loadBirthdaysFromVk() {
        val dataVk = dataCache.getBirthdaysFromVkByPeriod(getStartDate(), getEndDate())
        if (dataVk.isNotEmpty()) {
            addHeaderRow("Дни рождения друзей ВКонтакте")
            data.addAll(dataVk)
        }
    }

    override suspend fun loadScheduledEvents() {
        val scheduledEvents = dataCache.getScheduledEventsByPeriod(getStartDate(), getEndDate())
        if (scheduledEvents.isNotEmpty()) {
            addHeaderRow("Запланированные события")
            data.addAll(scheduledEvents)
        }
    }

    private fun addHeaderRow(name: String) {
        data.add(DataModel.SimpleNotice(name, ""))
    }
}