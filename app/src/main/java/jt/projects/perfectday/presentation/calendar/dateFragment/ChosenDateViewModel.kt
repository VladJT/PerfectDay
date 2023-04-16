package jt.projects.perfectday.presentation.calendar.dateFragment

import jt.projects.model.DataModel
import jt.projects.perfectday.core.BaseViewModel
import jt.projects.perfectday.interactors.BirthdayFromPhoneInteractorImpl
import jt.projects.perfectday.interactors.GetFriendsFromVkUseCase
import jt.projects.perfectday.interactors.ScheduledEventInteractorImpl
import jt.projects.utils.chosenCalendarDate
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences

class ChosenDateViewModel(
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

    private var birthdaysFromPhoneChosenDate: List<DataModel>? = null
    private var birthdaysFromVKChosenDate: List<DataModel>? = null

    override suspend fun loadBirthdaysFromPhone() {
        if (birthdaysFromPhoneChosenDate == null) {
            birthdaysFromPhoneChosenDate =
                birthdayFromPhoneInteractor.getContactsByDay(chosenCalendarDate)
        }
        if (birthdaysFromPhoneChosenDate!!.isNotEmpty()) {
            addHeaderRow("Дни рождения контактов")
            data.addAll(birthdaysFromPhoneChosenDate ?: listOf())
        }
    }

    override suspend fun loadBirthdaysFromVk() {
        if (birthdaysFromVKChosenDate == null) {
            birthdaysFromVKChosenDate =
                getFriendsFromVkUseCase.getFriendsByDate(
                    vkToken,
                    chosenCalendarDate
                )
        }
        if (birthdaysFromVKChosenDate!!.isNotEmpty()) {
            addHeaderRow("Дни рождения Вконтакте")
            data.addAll(birthdaysFromVKChosenDate ?: listOf())
        }
    }

    override suspend fun loadScheduledEvents() {
        val scheduledEvents =
            scheduledEventInteractorImpl.getScheduledEventsByDate(chosenCalendarDate)

        if (scheduledEvents.isNotEmpty()) {
            addHeaderRow("Запланированные события")
            data.addAll(scheduledEvents)
        }
    }

    fun addHeaderRow(name: String) {
        data.add(DataModel.SimpleNotice(name, ""))
    }

    override fun preparePostValue() {
        super.preparePostValue()
    }
}