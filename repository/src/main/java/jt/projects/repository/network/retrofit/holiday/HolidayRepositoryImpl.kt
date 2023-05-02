package jt.projects.repository.network.retrofit.holiday

import jt.projects.repository.network.retrofit.*
import jt.projects.repository.network.retrofit.holiday.dto.HolidayDTO
import jt.projects.utils.HOLIDAY_BASE_URL_LOCATION
import retrofit2.Retrofit
import java.time.LocalDate


class HolidayRepositoryImpl(retrofit: Retrofit):HolidayRepository {
    private val api = retrofit.newBuilder()
        .baseUrl(HOLIDAY_BASE_URL_LOCATION)
        .build()
        .create(HolidayApi::class.java)

    override suspend fun getHoliday(country: String, date: LocalDate): List<HolidayDTO> =
        api.searchHoliday(
            country,
            date.year.toString(),
            date.monthValue.toString(),
            date.dayOfMonth.toString()
        )
}