package jt.projects.repository.network.holiday

import jt.projects.repository.network.holiday.dto.HolidayDTO
import jt.projects.repository.network.holiday.dto.calendarific.Holiday
import jt.projects.repository.network.holiday.dto.calendarific.Response
import jt.projects.utils.CALENDARIFIC_BASE_URL_LOCATION
import jt.projects.utils.HOLIDAY_BASE_URL_LOCATION
import retrofit2.Retrofit
import java.time.LocalDate


class HolidayRepositoryImpl(retrofit: Retrofit) : HolidayRepository {
    private val apiH = retrofit.newBuilder()
        .baseUrl(HOLIDAY_BASE_URL_LOCATION)
        .build()
        .create(HolidayApi::class.java)

    private val apiC = retrofit.newBuilder()
        .baseUrl(CALENDARIFIC_BASE_URL_LOCATION)
        .build()
        .create(HolidayApi::class.java)


    override suspend fun getHoliday(country: String, date: LocalDate): List<HolidayDTO> =
        apiH.searchHoliday(
            country,
            date.year.toString(),
            date.monthValue.toString(),
            date.dayOfMonth.toString()
        )

    override suspend fun getHolidayFromCalendarific(
        country: String,
        date: LocalDate
    ): Holiday =
        apiC.searchHolidayCaledarific(
            country,
            date.year.toString(),
            date.monthValue.toString(),
            date.dayOfMonth.toString()
        )

}