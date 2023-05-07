package jt.projects.repository.network.holiday

import jt.projects.repository.network.holiday.dto.HolidayDTO
import jt.projects.repository.network.holiday.dto.calendarific.Holiday
import jt.projects.repository.network.holiday.dto.calendarific.Response
import java.time.LocalDate

interface HolidayRepository {
    suspend fun getHoliday(country:String, date: LocalDate): List<HolidayDTO>
    suspend fun getHolidayFromCalendarific(country:String, date: LocalDate): Holiday
}