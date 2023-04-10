package jt.projects.perfectday.interactors

import jt.projects.model.DataModel
import jt.projects.repository.network.mapper.parseArrayDTOtoDataModel
import jt.projects.repository.network.retrofit.holiday.HolidayRepository
import java.time.LocalDate
import jt.projects.perfectday.BuildConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single

class HolidayInteractorImpl(
    private val repository: HolidayRepository
) {

    suspend fun getHolidayByDate(date: LocalDate, country: String): List<DataModel.Holiday> {
        val apiKey = BuildConfig.HOLIDAY_API_KEY
        val respose = parseArrayDTOtoDataModel(repository.getHoliday(apiKey, country, date))
        return respose
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