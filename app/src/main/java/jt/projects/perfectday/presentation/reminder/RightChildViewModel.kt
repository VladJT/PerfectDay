package jt.projects.perfectday.presentation.reminder

import jt.projects.perfectday.core.BaseViewModel
import jt.projects.perfectday.core.DateStrategy
import jt.projects.perfectday.core.PhoneBookProvider
import jt.projects.perfectday.interactors.BirthdayFromPhoneInteractorImpl
import jt.projects.perfectday.interactors.GetFriendsFromVkUseCase
import jt.projects.perfectday.interactors.ScheduledEventInteractorImpl
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences
import java.time.LocalDate

class RightChildViewModel(
    settingsPreferences: SimpleSettingsPreferences,
    birthdayFromPhoneInteractor: BirthdayFromPhoneInteractorImpl,
    getFriendsFromVkUseCase: GetFriendsFromVkUseCase,
    scheduledEventInteractor: ScheduledEventInteractorImpl,
    phoneBookProvider: PhoneBookProvider,
    dateStrategy: DateStrategy
) :
    BaseViewModel(
        settingsPreferences,
        birthdayFromPhoneInteractor,
        getFriendsFromVkUseCase,
        scheduledEventInteractor,
        phoneBookProvider,
        dateStrategy
    ) {
}