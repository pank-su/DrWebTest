package su.pank.drwebtest.database

import androidx.room.Database
import androidx.room.RoomDatabase
import su.pank.drwebtest.database.dao.HashDao
import su.pank.drwebtest.database.model.HashEntity

@Database(version = 1, entities = [HashEntity::class])
abstract class HashDataBase: RoomDatabase() {
    abstract fun hashDao(): HashDao
}