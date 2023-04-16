package jt.projects.perfectday.presentation.calendar

import jt.projects.perfectday.core.BaseViewModel
import jt.projects.perfectday.core.AppDataCache
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences

class CalendarViewModel(
    settingsPreferences: SimpleSettingsPreferences,
    dataCache: AppDataCache
) : BaseViewModel(
    settingsPreferences,
    dataCache
) {


    override suspend fun loadBirthdaysFromPhone() {
        val allData = dataCache.getAllBirthdaysFromPhone()
        data.addAll(allData)
    }

    override suspend fun loadBirthdaysFromVk() {
        val allData = dataCache.getAllBirthdaysFromVk()
        data.addAll(allData)
    }

    override suspend fun loadScheduledEvents() {
        val allData = dataCache.getAllScheduledEvents()
        data.addAll(allData)
    }
}