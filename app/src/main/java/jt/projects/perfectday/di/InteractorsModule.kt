package jt.projects.perfectday.di

import jt.projects.perfectday.interactors.*
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val interactorsModule = module {
    single { BirthdayFromPhoneInteractorImpl(androidApplication().applicationContext) }
    single { SimpleNoticeInteractorImpl(repository = get()) }
    single { GetFriendsFromVkUseCase(vkNetworkRepository = get()) }
    single { ScheduledEventInteractorImpl(repository = get()) }
    single { HolidayInteractorImpl(repository = get()) }
}