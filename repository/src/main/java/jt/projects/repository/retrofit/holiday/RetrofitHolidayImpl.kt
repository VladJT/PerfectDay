package jt.projects.repository.retrofit.holiday

import jt.projects.repository.retrofit.BaseInterceptor
import jt.projects.repository.retrofit.BaseRetrofit
import jt.projects.repository.retrofit.DataSource
import jt.projects.repository.retrofit.holiday.dto.HolidayDTO
import jt.projects.repository.retrofit.holiday.dto.HolidayDTOItem
import jt.projects.utils.HOLIDAY_BASE_URL_LOCATION
import kotlinx.coroutines.flow.Flow

class RetrofitHolidayImpl: BaseRetrofit(), DataSource<Flow<HolidayDTO>> {

    override suspend fun getData(): Flow<HolidayDTO> {

        return getService(BaseInterceptor.interceptor, HOLIDAY_BASE_URL_LOCATION).searchHoliday("","",0,0,0)
    }



}