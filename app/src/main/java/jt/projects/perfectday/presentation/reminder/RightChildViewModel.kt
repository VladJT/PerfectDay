package jt.projects.perfectday.presentation.reminder

import jt.projects.perfectday.interactors.BirthdayFromPhoneInteractorImpl
import jt.projects.perfectday.interactors.GetFriendsFromVkUseCase
import jt.projects.perfectday.interactors.ScheduledEventInteractorImpl
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences
import java.time.LocalDate

class RightChildViewModel(
    settingsPreferences: SimpleSettingsPreferences,
    birthdayFromPhoneInteractor: BirthdayFromPhoneInteractorImpl,
    getFriendsFromVkUseCase: GetFriendsFromVkUseCase,
    scheduledEventInteractor: ScheduledEventInteractorImpl
) :
    BaseChildViewModel(
        settingsPreferences,
        birthdayFromPhoneInteractor,
        getFriendsFromVkUseCase,
        scheduledEventInteractor
    ) {

    override fun getStartDate(): LocalDate = LocalDate.now()
    override fun getEndDate(): LocalDate =
        getStartDate().plusDays(settingsPreferences.getDaysPeriodForReminderFragment())
}