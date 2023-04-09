package jt.projects.repository.network.mapper

import jt.projects.model.DataModel
import jt.projects.repository.network.retrofit.holiday.dto.HolidayDTOItem

internal fun parseArrayDTOtoDataModel(holidayDTO:ArrayList<HolidayDTOItem>){
    var listDataModel = mutableListOf<DataModel.Holiday>()
    if (holidayDTO.isNotEmpty()){
        holidayDTO.forEach {
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