package su.pank.drwebtest.domain

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import su.pank.drwebtest.data.hash.HashRepository
import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest

class ApkHashUseCase(private val context: Context, private val hashRepository: HashRepository) {

    suspend operator fun invoke(packageName: String): String? {
        val packageInfo = context.packageManager.getPackageInfo(packageName, 0)
        val appHash = hashRepository.hashByPackageName(packageName)
        if (appHash?.updateTime == packageInfo.lastUpdateTime) return appHash.hash
        val sourceDir = packageInfo.applicationInfo?.sourceDir ?: return null
        val hash = hashByApk(sourceDir)
        hashRepository.upsertHash(packageName, hash)
        return hash
    }

    suspend fun hashByApk(sourceDir: String, algorithm: String = "SHA-256"): String {
        val file = File(sourceDir)
        val digest = MessageDigest.getInstance(algorithm)
        return withContext(Dispatchers.IO) {
            FileInputStream(file).use {
                val buffer = ByteArray(1024 * 8)

                do {
                    val read = it.read(buffer)

                    if (read != -1) digest.update(buffer, 0, read)
                } while (read != -1)
            }

            digest.digest().toHexString()
        }
    }

}