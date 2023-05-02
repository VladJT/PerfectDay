package jt.projects.perfectday.interactors

import jt.projects.model.DataModel
import jt.projects.repository.network.mapper.parseArrayDTOtoDataModel
import jt.projects.repository.network.holiday.HolidayRepository
import jt.projects.utils.HOLIDAY_COUNTRY
import java.time.LocalDate

class HolidayInteractorImpl(
    private val repository: HolidayRepository
) {

    suspend fun getHolidayByDate(date: LocalDate, country: String = HOLIDAY_COUNTRY): List<DataModel.Holiday> {
        return parseArrayDTOtoDataModel(repository.getHoliday(country, date))
    }
}

