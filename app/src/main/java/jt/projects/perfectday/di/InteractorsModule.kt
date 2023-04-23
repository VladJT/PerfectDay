package jt.projects.perfectday.di

import jt.projects.perfectday.interactors.BirthdayFromPhoneInteractorImpl
import jt.projects.perfectday.interactors.GetFriendsFromVkUseCase
import jt.projects.perfectday.interactors.HolidayInteractorImpl
import jt.projects.perfectday.interactors.ScheduledEventInteractorImpl
import jt.projects.perfectday.interactors.SimpleNoticeInteractorImpl
import jt.projects.repository.network.retrofit.DataSourceHoliday
import jt.projects.repository.network.retrofit.holiday.HolidayRepository
import jt.projects.repository.network.retrofit.holiday.HolidayRepositoryImpl
import jt.projects.repository.network.retrofit.holiday.RetrofitHolidayImpl
import jt.projects.repository.network.retrofit.holiday.dto.HolidayDTO
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val interactorsModule = module {
    single { BirthdayFromPhoneInteractorImpl(androidApplication().applicationContext) }
    single { SimpleNoticeInteractorImpl(repository = get()) }
    single { GetFriendsFromVkUseCase(vkNetworkRepository = get()) }
    single { ScheduledEventInteractorImpl(repository = get()) }

    //HolidayDi
    single { HolidayInteractorImpl(repository = get()) }
    single<HolidayRepository> { HolidayRepositoryImpl(dataSource = get()) }
    single<DataSourceHoliday<List<HolidayDTO>>> { RetrofitHolidayImpl() }
}