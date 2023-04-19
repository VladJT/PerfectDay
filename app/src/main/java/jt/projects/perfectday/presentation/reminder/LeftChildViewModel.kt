package jt.projects.perfectday.presentation.reminder

import jt.projects.perfectday.core.viewholders.PhoneBookProvider
import jt.projects.perfectday.interactors.BirthdayFromPhoneInteractorImpl
import jt.projects.perfectday.interactors.GetFriendsFromVkUseCase
import jt.projects.perfectday.interactors.ScheduledEventInteractorImpl
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences
import java.time.LocalDate

class LeftChildViewModel(
    settingsPreferences: SimpleSettingsPreferences,
    birthdayFromPhoneInteractor: BirthdayFromPhoneInteractorImpl,
    getFriendsFromVkUseCase: GetFriendsFromVkUseCase,
    scheduledEventInteractor: ScheduledEventInteractorImpl,
    phoneBookProvider: PhoneBookProvider
) :
    BaseChildViewModel(
        settingsPreferences,
        birthdayFromPhoneInteractor,
        getFriendsFromVkUseCase,
        scheduledEventInteractor,
        phoneBookProvider
    ) {

    override fun getStartDate(): LocalDate = LocalDate.now()
    override fun getEndDate(): LocalDate = getStartDate().plusDays(1)
}