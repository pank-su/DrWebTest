package su.pank.drwebtest.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hash")
data class HashEntity(
    @PrimaryKey val packageName: String,
    val updateTime: Long,
    val hash: String
)