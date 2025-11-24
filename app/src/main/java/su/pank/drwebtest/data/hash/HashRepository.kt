package su.pank.drwebtest.data.hash

import kotlinx.coroutines.flow.Flow
import su.pank.drwebtest.data.model.AppHash

interface HashRepository {
    suspend fun hashByPackageName(packageName: String): AppHash?
    suspend fun upsertHash(packageName: String, hash: String)
}