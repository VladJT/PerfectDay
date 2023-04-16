package jt.projects.perfectday.presentation.calendar.dateFragment

import jt.projects.model.DataModel
import jt.projects.perfectday.core.BaseViewModel
import jt.projects.perfectday.core.AppDataCache
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
            addHeaderRow("Дни рождения контактов телефона")
            data.addAll(dataPhone)
        }
    }

    override suspend fun loadBirthdaysFromVk() {
        val dataVk = dataCache.getBirthdaysFromVkByDate(chosenCalendarDate)
        if (dataVk.isNotEmpty()) {
            addHeaderRow("Дни рождения друзей ВКонтакте")
            data.addAll(dataVk)
        }
    }

    override suspend fun loadScheduledEvents() {
        val scheduledEvents = dataCache.getScheduledEventsByDate(chosenCalendarDate)
        if (scheduledEvents.isNotEmpty()) {
            addHeaderRow("Запланированные события")
            data.addAll(scheduledEvents)
        }
    }

    fun addHeaderRow(name: String) {
        data.add(DataModel.SimpleNotice(name, ""))
    }
}