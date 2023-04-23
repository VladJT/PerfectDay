package jt.projects.perfectday.di

import jt.projects.repository.room.LocalRepository
import jt.projects.repository.room.RoomDatabaseImpl
import org.koin.dsl.module

val repoModule = module {
    single<LocalRepository> { RoomDatabaseImpl(dao = get()) }
}