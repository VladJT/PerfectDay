package jt.projects.repository.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ScheduledEventEntity::class], version = 1, exportSchema = true)
abstract class ScheduledEventDatabase : RoomDatabase() {
    abstract fun dao(): ScheduledEventDao
}