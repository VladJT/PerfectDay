package jt.projects.repository.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// ключевое слово suspend, которое намекает, что все
// запросы в БД будут асинхронными (корутины поддерживаются в Room изначально)

@Dao
interface ScheduledEventDao {
    /**
     * SELECT
     */
    @Query("select * from ScheduledEventEntity")
    suspend fun getAll(): List<ScheduledEventEntity>

    @Query("SELECT * FROM ScheduledEventEntity WHERE date = :date")
    fun getNotesByDate(date: String): Flow<List<ScheduledEventEntity>>

    @Query("select * from ScheduledEventEntity where id = :id")
    suspend fun getDataById(id: Int): ScheduledEventEntity?


    /**
     * INSERT
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE) // дубликаты не будут сохраняться
    suspend fun insert(entity: ScheduledEventEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(entities: List<ScheduledEventEntity>)


    /**
     * UPDATE
     */
    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(entity: ScheduledEventEntity)


    /**
     * DELETE
     */
    @Delete
    suspend fun delete(entity: ScheduledEventEntity)

    @Query("delete from ScheduledEventEntity where id = :id")
    suspend fun deleteById(id: Int)

    @Query("delete from ScheduledEventEntity")
    suspend fun deleteAll()
}