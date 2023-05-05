package jt.projects.repository.network.mapper

import jt.projects.model.DataModel
import jt.projects.repository.network.holiday.dto.HolidayDTO
import jt.projects.repository.network.holiday.dto.calendarific.HolidayCalendarificDto
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

fun parseCalendarificDTOtoDataModel(holidayDTO: List<HolidayCalendarificDto>): List<DataModel.Holiday> {
    return holidayDTO.map {
        DataModel.Holiday(
            name = it.name,
            country = "Russia",
            type = it.type,
            description = it.description,
            date = LocalDate.now()
        )
    }
}
