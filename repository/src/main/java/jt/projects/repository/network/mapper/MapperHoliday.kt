package jt.projects.repository.network.mapper

import jt.projects.model.DataModel
import jt.projects.repository.network.holiday.dto.HolidayDTO
import jt.projects.repository.network.holiday.dto.calendarific.Holiday
import jt.projects.repository.network.holiday.dto.calendarific.Response
import java.time.LocalDate

fun parseArrayDTOtoDataModel(holidayDTO: List<HolidayDTO>): List<DataModel.Holiday> {
    return holidayDTO.map {
        DataModel.Holiday(
            name = it.name,
            country = it.country,
            type = it.type,
            description = it.description,
            date = LocalDate.parse("${it.dateYear}-${it.dateMonth}-${it.dateDay}")
        )
    }
}

fun parseCalendarificDTOtoDataModel(response: Holiday): List<DataModel.Holiday> {
    if (response.response.holidays.isNullOrEmpty()) {
        return listOf()
    }

    return response.response.holidays.map {
        DataModel.Holiday(
            name = it.name,
            country = "Russia",
            type = it.type,
            description = it.description,
            date = LocalDate.now()
        )
    }
}




