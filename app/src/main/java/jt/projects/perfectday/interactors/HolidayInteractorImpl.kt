package jt.projects.perfectday.interactors

import jt.projects.model.DataModel
import jt.projects.repository.network.mapper.parseArrayDTOtoDataModel
import jt.projects.repository.network.retrofit.holiday.HolidayRepository
import jt.projects.utils.HOLIDAY_COUNTRY
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single
import java.time.LocalDate

class HolidayInteractorImpl(
    private val repository: HolidayRepository
) {

    suspend fun getHolidayByDate(date: LocalDate, country: String = HOLIDAY_COUNTRY): List<DataModel.Holiday> {
        return parseArrayDTOtoDataModel(repository.getHoliday(country, date))
    }

    suspend fun getFakeHoliday(): List<DataModel.Holiday> {
        return flow {
            emit(
                listOf(
                    DataModel.Holiday(
                        country = "Россия",
                        name = "День труда",
                        description = "День всех трудящихся",
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
            )
        }.single().distinctBy { it.name }
    }
}

