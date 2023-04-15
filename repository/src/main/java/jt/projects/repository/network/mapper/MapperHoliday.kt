package jt.projects.repository.network.mapper

import jt.projects.model.DataModel
import jt.projects.repository.network.retrofit.holiday.dto.HolidayDTO

fun parseArrayDTOtoDataModel(holidayDTO: List<HolidayDTO>): List<DataModel.Holiday> {
    return holidayDTO.map {
        DataModel.Holiday(
            name = it.name,
            country = it.country,
            type = it.type,
            description = it.description,
            photoUrl = ""
        )
    }
}