package su.pank.drwebtest.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import su.pank.drwebtest.database.model.HashEntity

@Dao
interface HashDao {
    @Upsert(HashEntity::class)
    fun upsertHash(newHash: HashEntity)

    @Query("SELECT * FROM hash WHERE packageName = :packageName")
    fun getHashByPackageName(packageName: String): HashEntity?


}