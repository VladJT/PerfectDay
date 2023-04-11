package jt.projects.repository.network.mapper

import jt.projects.model.DataModel
import jt.projects.repository.network.retrofit.holiday.dto.HolidayDTO
import jt.projects.repository.network.retrofit.holiday.dto.HolidayDTOItem

fun parseArrayDTOtoDataModel(holidayDTO:List<HolidayDTO>):List<DataModel.Holiday>{
    var listDataModel = mutableListOf<DataModel.Holiday>()
    if (holidayDTO.isNotEmpty()){
        holidayDTO.forEach { it ->
            it.forEach{
                listDataModel.add(
                    DataModel.Holiday(
                        name = it.name,
                        country = it.country,
                        type = it.type,
                        description = it.description,
                        photoUrl = ""
                    )
                )
            }
        }
    }
    return listDataModel
}