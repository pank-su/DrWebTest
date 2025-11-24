package su.pank.drwebtest.data.hash

import android.content.Context
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import su.pank.drwebtest.data.model.AppHash
import su.pank.drwebtest.database.dao.HashDao
import su.pank.drwebtest.database.model.HashEntity

class LocalHashRepository(
    private val hashDao: HashDao,
    private val context: Context,
    private val dispatcher: CoroutineDispatcher
) :
    HashRepository {
    override suspend fun hashByPackageName(packageName: String): AppHash? {

        return withContext(dispatcher) {
            hashDao.getHashByPackageName(packageName)?.let {
                AppHash(it.hash, it.updateTime)
            }
        }
    }

    override suspend fun upsertHash(packageName: String, hash: String) {
        withContext(dispatcher) {
            hashDao.upsertHash(HashEntity(packageName, getLastUpdateTime(packageName), hash))
        }
    }


    private fun getLastUpdateTime(packageName: String): Long {
        return context.packageManager.getPackageInfo(packageName, 0).lastUpdateTime
    }
}