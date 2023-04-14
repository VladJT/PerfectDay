package jt.projects.perfectday.presentation.reminder

import jt.projects.model.DataModel
import jt.projects.perfectday.core.BaseViewModel
import jt.projects.perfectday.interactors.BirthdayFromPhoneInteractorImpl
import jt.projects.perfectday.interactors.GetFriendsFromVkUseCase
import jt.projects.perfectday.interactors.ScheduledEventInteractorImpl
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences
import java.time.LocalDate

class ReminderViewModel(
    settingsPreferences: SimpleSettingsPreferences,
    birthdayFromPhoneInteractor: BirthdayFromPhoneInteractorImpl,
    getFriendsFromVkUseCase: GetFriendsFromVkUseCase,
    scheduledEventInteractorImpl: ScheduledEventInteractorImpl
) :
    BaseViewModel(
        settingsPreferences,
        birthdayFromPhoneInteractor,
        getFriendsFromVkUseCase,
        scheduledEventInteractorImpl
    ) {

    var isShowTomorrow = true

    var currentEndDate = LocalDate.now()

    // Phone CACHE
    private var cachedBirthdaysFromPhoneTomorrow: List<DataModel>? = null
    private var cachedBirthdaysFromPhonePeriod: List<DataModel>? = null

    // VK CACHE
    private var cachedBirthdaysFromVkTomorrow: List<DataModel>? = null
    private var cachedBirthdaysFromVkPeriod: List<DataModel>? = null

    private fun getStartDate(): LocalDate {
        return if (isShowTomorrow) LocalDate.now().plusDays(1)
        else LocalDate.now()
    }

    private fun getEndDate(): LocalDate {
        return if (isShowTomorrow) getStartDate()
        else getStartDate().plusDays(settingsPreferences.getDaysPeriodForReminderFragment())
    }


    override suspend fun loadBirthdaysFromPhone() {
        if (isShowTomorrow) {
            // если уже получали данные - подгружаем их из кэша
            if (cachedBirthdaysFromPhoneTomorrow == null) {
                cachedBirthdaysFromPhoneTomorrow =
                    birthdayFromPhoneInteractor.getContactsInInterval(getStartDate(), getEndDate())
            }
            if (cachedBirthdaysFromPhoneTomorrow!!.isNotEmpty()) {
                addHeaderRow("Дни рождения контактов")
                data.addAll(cachedBirthdaysFromPhoneTomorrow ?: listOf())
            }
        } else {
            // если уже НЕ получали данные или изменился период - подгружаем список заново
            if (cachedBirthdaysFromPhonePeriod == null || currentEndDate != getEndDate()) {
                cachedBirthdaysFromPhonePeriod =
                    birthdayFromPhoneInteractor.getContactsInInterval(
                        getStartDate(),
                        getEndDate()
                    )
            }
            currentEndDate = getEndDate()
            if (cachedBirthdaysFromPhonePeriod!!.isNotEmpty()) {
                addHeaderRow("Дни рождения контактов")
                data.addAll(cachedBirthdaysFromPhonePeriod ?: listOf())
            }
        }
    }

    override suspend fun loadBirthdaysFromVk() {
        if (isShowTomorrow) {
            // если уже получали данные - подгружаем их из кэша
            if (cachedBirthdaysFromVkTomorrow == null) {
                cachedBirthdaysFromVkTomorrow =
                    getFriendsFromVkUseCase.getFriendsByPeriodDate(
                        vkToken,
                        getStartDate(),
                        getEndDate()
                    )
            }
            if (cachedBirthdaysFromVkTomorrow!!.isNotEmpty()) {
                addHeaderRow("Дни рождения друзей ВКонтакте")
                data.addAll(cachedBirthdaysFromVkTomorrow ?: listOf())
            }
        } else {
            // если уже НЕ получали данные или изменился период - подгружаем список заново
            if (cachedBirthdaysFromVkPeriod == null || currentEndDate != getEndDate()) {
                cachedBirthdaysFromVkPeriod =
                    getFriendsFromVkUseCase.getFriendsByPeriodDate(
                        vkToken,
                        getStartDate(),
                        getEndDate()
                    )
            }
            currentEndDate = getEndDate()
            if (cachedBirthdaysFromVkPeriod!!.isNotEmpty()) {
                addHeaderRow("Дни рождения друзей ВКонтакте")
                data.addAll(cachedBirthdaysFromVkPeriod ?: listOf())
            }
        }
    }

    override suspend fun loadScheduledEvents() {
        val scheduledEvents =
            scheduledEventInteractorImpl.getScheduledEventsByPeriod(getStartDate(), getEndDate())

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
        //   data.sortBy {  }
    }

}