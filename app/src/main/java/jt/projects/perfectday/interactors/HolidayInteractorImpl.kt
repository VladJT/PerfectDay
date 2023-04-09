package jt.projects.perfectday.interactors

import jt.projects.model.DataModel
import jt.projects.repository.network.retrofit.holiday.HolidayRepository
import jt.projects.repository.network.retrofit.holiday.dto.HolidayDTO
import java.time.LocalDate

class HolidayInteractorImpl(
    private val repository: HolidayRepository
) {

    suspend fun getHolidayByDate(date: LocalDate, country: String): List<HolidayDTO> {
        val apiKey = "88a2a8cc787c4a5bb28b5367c9753c2d"
        return repository.getHoliday(apiKey, country, date)
    }

    fun getFakeHoliday(): List<DataModel.Holiday> {
        return listOf(
            DataModel.Holiday(
                country = "Россия",
                name = "День труда",
                description = "День трудящихся",
                photoUrl = "",
                type = "Национальный"
            ),
            DataModel.Holiday(
                country = "Россия",
                name = "День победы",
                description = "День победы в ВОВ",
                photoUrl = "",
                type = "Национальный"
            )

            )
    }
}