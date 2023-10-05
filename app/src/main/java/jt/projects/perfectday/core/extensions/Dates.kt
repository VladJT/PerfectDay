package jt.projects.perfectday.core.extensions

import jt.projects.model.DataModel
import jt.projects.perfectday.presentation.today.adapter.birth.FriendItem
import jt.projects.utils.DATE_FORMAT_DAY_MONTH_YEAR
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

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