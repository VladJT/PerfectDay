package jt.projects.perfectday.interactors

import jt.projects.model.DataModel
import jt.projects.perfectday.core.translator.translateText
import jt.projects.repository.network.holiday.HolidayRepository
import jt.projects.repository.network.mapper.parseArrayDTOtoDataModel
import jt.projects.repository.network.mapper.parseCalendarificDTOtoDataModel
import jt.projects.utils.HOLIDAY_COUNTRY
import java.time.LocalDate
import java.util.Locale

class HolidayInteractorImpl(
    private val repository: HolidayRepository
) {

    suspend fun getHolidayByDate(
        date: LocalDate,
        country: String = HOLIDAY_COUNTRY
    ): List<DataModel.Holiday> {
        return parseArrayDTOtoDataModel(repository.getHoliday(country, date))
    }

    suspend fun getCalendarificHolidayByDate(
        date: LocalDate,
        country: String = HOLIDAY_COUNTRY
    ): List<DataModel.Holiday> {

        val result = mutableListOf<DataModel.Holiday>()
        val source =
            parseCalendarificDTOtoDataModel(repository.getHolidayFromCalendarific(country, date))
        source.forEach { result.add(it) }
        val finalResult = result.distinctBy { it.description }

        if (Locale.getDefault().language.equals("ru")) {
            finalResult.forEach {
                it.description = it.description?.translateText()
                it.name = it.name.translateText()
            }
        }

        return finalResult
    }

}

