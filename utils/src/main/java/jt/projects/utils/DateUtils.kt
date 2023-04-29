package jt.projects.utils

import jt.projects.model.DataModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


fun LocalDate.toStdFormatString(): String {
    return this.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
}

fun String.toStdLocalDate(): LocalDate {
    return LocalDate.parse(this, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
}

fun getAlertStringHowManyDaysBefore(birthDate: LocalDate): String {
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
        0L -> "Сегодня"
        1L -> "Завтра"
        2L -> "Послезавтра"
        else -> "$daysBeforeEventCount дней до события"
    }
}

fun getAlertStringHowManyDaysBeforeScheduledEvent(eventDate: LocalDate): String {
    var daysBeforeEventCount = ChronoUnit.DAYS.between(LocalDate.now(), eventDate)

    if (daysBeforeEventCount < 0) return "Событие прошло"

    return when (daysBeforeEventCount) {
        0L -> "Сегодня"
        1L -> "Завтра"
        2L -> "Послезавтра"
        else -> "$daysBeforeEventCount дней до события"
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
