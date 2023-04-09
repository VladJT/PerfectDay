package jt.projects.repository.network.retrofit.holiday

import jt.projects.repository.network.retrofit.BaseInterceptor
import jt.projects.repository.network.retrofit.BaseRetrofit
import jt.projects.repository.network.retrofit.DataSourceHoliday
import jt.projects.repository.network.retrofit.holiday.dto.HolidayDTO
import jt.projects.utils.HOLIDAY_BASE_URL_LOCATION
import java.time.LocalDate

class RetrofitHolidayImpl: BaseRetrofit(), DataSourceHoliday<List<HolidayDTO>> {

    override suspend fun getData(apiKey:String, country:String, date: LocalDate): List<HolidayDTO> {

        return getService(BaseInterceptor.interceptor, HOLIDAY_BASE_URL_LOCATION).searchHoliday(apiKey,country,date.year,date.monthValue,date.dayOfMonth).await()
    }



}