package jt.projects.perfectday.di

import androidx.room.Room
import jt.projects.repository.room.ScheduledEventDatabase
import org.koin.dsl.module

val roomModule = module {
    single {
        Room.databaseBuilder(get(), ScheduledEventDatabase::class.java, "scheduledEvents.db")
            .build()
    }

    single { get<ScheduledEventDatabase>().dao() }
}