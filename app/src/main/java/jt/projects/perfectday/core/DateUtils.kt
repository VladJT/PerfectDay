package jt.projects.perfectday.core

import jt.projects.model.DataModel
import jt.projects.perfectday.App
import jt.projects.perfectday.R
import jt.projects.perfectday.presentation.today.adapter.birth.FriendItem
import jt.projects.utils.DATE_FORMAT_DAY_MONTH_YEAR
import jt.projects.utils.DATE_FORMAT_DAY_MONTH_YEAR_RUS
import org.koin.java.KoinJavaComponent.getKoin
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit


fun LocalDate.toStdFormatString(): String {
    return this.format(DateTimeFormatter.ofPattern(DATE_FORMAT_DAY_MONTH_YEAR_RUS))
}

fun String.toStdLocalDate(): LocalDate {
    return LocalDate.parse(this, DateTimeFormatter.ofPattern(DATE_FORMAT_DAY_MONTH_YEAR_RUS))
}

fun getAlertStringHowManyDaysBefore(birthDate: LocalDate): String {
    val context = getKoin().get<App>()

    var endDate = LocalDate.of(
        LocalDate.now().year,
        birthDate.month,
        birthDate.dayOfMonth
    )
    var daysBeforeEventCount = ChronoUnit.DAYS.between(LocalDate.now(), endDate)

    if (daysBeforeEventCount < 0) endDate = LocalDate.of(
        LocalDate.now().year.plus(1),
        birthDate.month,
        birthDate.dayOfMonth
    )

    daysBeforeEventCount = ChronoUnit.DAYS.between(LocalDate.now(), endDate)

    return when (daysBeforeEventCount) {
        0L -> context.getString(R.string.today)
        1L -> context.getString(R.string.tomorrow)
        2L -> context.getString(R.string.day_after_tomorrow)
        else -> "$daysBeforeEventCount ".plus(context.getString(R.string.days_before_event))
    }
}

fun getAlertStringHowManyDaysBeforeScheduledEvent(eventDate: LocalDate): String {
    val context = getKoin().get<App>()

    var daysBeforeEventCount = ChronoUnit.DAYS.between(LocalDate.now(), eventDate)

    if (daysBeforeEventCount < 0) return context.getString(R.string.event_passed)

    return when (daysBeforeEventCount) {
        0L -> context.getString(R.string.today)
        1L -> context.getString(R.string.tomorrow)
        2L -> context.getString(R.string.day_after_tomorrow)
        else -> "$daysBeforeEventCount ".plus(context.getString(R.string.days_before_event))
    }
}



fun isPeriodBirthdayDate(
    startDate: LocalDate,
    endDate: LocalDate,
    birthDate: LocalDate
): Boolean = isDateAfterStart(startDate, birthDate) && isDateBeforeEndDate(endDate, birthDate)

fun isDateAfterStart(startDate: LocalDate, birthDate: LocalDate): Boolean =
    LocalDate.of(LocalDate.now().year, birthDate.month, birthDate.dayOfMonth) >= startDate

fun isDateBeforeEndDate(endDate: LocalDate, birthDate: LocalDate): Boolean =
    LocalDate.of(LocalDate.now().year, birthDate.month, birthDate.dayOfMonth) <= endDate

val sortComparatorByMonthAndDay = Comparator<DataModel> { o1, o2 ->
    if (o1 is DataModel.BirthdayFromVk && o2 is DataModel.BirthdayFromVk) {
        val date1 = o1.birthDate
        val date2 = o2.birthDate
        val month1 = date1.monthValue
        val month2 = date2.monthValue

        if (month1 == month2) return@Comparator date1.dayOfMonth.compareTo(date2.dayOfMonth)
        return@Comparator month1.compareTo(month2)
    } else {
        val b1 = o1 as DataModel.BirthdayFromPhone
        val b2 = o2 as DataModel.BirthdayFromPhone

        val date1 = o1.birthDate
        val date2 = o2.birthDate
        val month1 = date1.monthValue
        val month2 = date2.monthValue

        if (month1 == month2) return@Comparator date1.dayOfMonth.compareTo(date2.dayOfMonth)
        return@Comparator month1.compareTo(month2)
    }
}

// сортировка ДР по возрастанию от текущей даты
fun List<FriendItem>.sortedByBirthDay(): List<FriendItem> {
    val thisYearList = mutableListOf<FriendItem>()
    val nextYearList = mutableListOf<FriendItem>()
    val startIntervalDate = LocalDate.now()

    this.forEach {
        if (startIntervalDate <= LocalDate.of(
                startIntervalDate.year,
                it.birthDate.monthValue,
                it.birthDate.dayOfMonth
            )
        ) {
            thisYearList.add(it)
        } else {
            nextYearList.add(it)
        }
    }

    thisYearList.sortBy {
        LocalDate.of(
            LocalDate.now().year,
            it.birthDate.monthValue,
            it.birthDate.dayOfMonth
        )
    }
    nextYearList.sortBy {
        LocalDate.of(
            LocalDate.now().year + 1,
            it.birthDate.monthValue,
            it.birthDate.dayOfMonth
        )
    }

    return thisYearList + nextYearList
}


fun sortListByDateDifferentYear(
    returnList: MutableList<DataModel.BirthdayFromPhone>,
    startIntervalDate: LocalDate
): List<DataModel.BirthdayFromPhone> {

    val thisYearList = mutableListOf<DataModel.BirthdayFromPhone>()
    val nextYearList = mutableListOf<DataModel.BirthdayFromPhone>()

    returnList.forEach {
        if (startIntervalDate <= LocalDate.of(
                startIntervalDate.year,
                it.birthDate.monthValue,
                it.birthDate.dayOfMonth
            )
        ) {
            thisYearList.add(it)
        } else {
            nextYearList.add(it)
        }
    }

    thisYearList.toList().sortedBy {
        LocalDate.of(
            LocalDate.now().year,
            it.birthDate.monthValue,
            it.birthDate.dayOfMonth
        )
    }
    nextYearList.toList().sortedBy {
        LocalDate.of(
            LocalDate.now().year + 1,
            it.birthDate.monthValue,
            it.birthDate.dayOfMonth
        )
    }

    return thisYearList + nextYearList
}

// определить возраст
fun getAge(birthDate: LocalDate): Int =
    Period.between(birthDate, LocalDate.now()).years.plus(1)


fun tryParseDate(date: String?): LocalDate {
    if (date == null) return LocalDate.MIN

    return try {
        LocalDate.parse(date, DateTimeFormatter.ofPattern(DATE_FORMAT_DAY_MONTH_YEAR))
    } catch (e: DateTimeParseException) {
        LocalDate.MIN
    }
}