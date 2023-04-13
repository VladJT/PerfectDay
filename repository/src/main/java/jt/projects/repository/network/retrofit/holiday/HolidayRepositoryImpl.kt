package jt.projects.repository.network.retrofit.holiday

import jt.projects.repository.network.retrofit.DataSourceHoliday
import jt.projects.repository.network.retrofit.holiday.dto.HolidayDTO
import java.time.LocalDate


class HolidayRepositoryImpl(private val dataSource : DataSourceHoliday<List<HolidayDTO>>):HolidayRepository {
    override suspend fun getHoliday(country:String,date: LocalDate): List<HolidayDTO> {
        return dataSource.getData(country,date)

    }
}