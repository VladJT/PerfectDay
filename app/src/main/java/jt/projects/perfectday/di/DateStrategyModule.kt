package jt.projects.perfectday.di

import jt.projects.perfectday.core.DateStrategy
import jt.projects.utils.DATE_STATEGY_ALLDATES
import jt.projects.utils.DATE_STATEGY_CHOSEN_CALENDER_DATE
import jt.projects.utils.DATE_STATEGY_PERIOD
import jt.projects.utils.DATE_STATEGY_TOMORROW
import jt.projects.utils.chosenCalendarDate
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.time.LocalDate

val dateStrategyModule = module {
    factory(named(DATE_STATEGY_ALLDATES)) { DateStrategy(null, null) }

    factory(named(DATE_STATEGY_TOMORROW)) {
        DateStrategy(
            startDate = { LocalDate.now() },
            endDate = { LocalDate.now().plusDays(1) })
    }

    factory(named(DATE_STATEGY_CHOSEN_CALENDER_DATE)) {
        DateStrategy(
            startDate = { chosenCalendarDate },
            endDate = { chosenCalendarDate })
    }

    factory(named(DATE_STATEGY_PERIOD)) {
        DateStrategy(
            startDate = { LocalDate.now() },
            endDate = {
                LocalDate.now()
                    .plusDays(get<SimpleSettingsPreferences>().getDaysPeriodForReminderFragment())
            })
    }
}