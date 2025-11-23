package su.pank.drwebtest.data.apps

import android.content.Context
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import su.pank.drwebtest.data.model.App
import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest

class DefaultAppsRepository(
    context: Context,
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AppsRepository {
    private val pm = context.packageManager

    // Можно сделать загрузку с помощью Pager
    override val apps: Flow<List<App>> = flow {
        pm.getInstalledApplications(0).map { info ->
            App(
                name = pm.getApplicationLabel(info).toString(),
                version = pm.getPackageInfo(info.packageName, 0).versionName
                    ?: "N/A",
                packageName = info.packageName,
                hashSum = getApkHash(info.sourceDir)
            )
        }.sortedBy { it.name }.also {
            emit(it)
        }
    }.flowOn(ioDispatcher)

    private fun getApkHash(sourceDir: String, algorithm: String = "SHA-256"): String {
        val file = File(sourceDir)
        val digest = MessageDigest.getInstance(algorithm)
        FileInputStream(file).use {
            val buffer = ByteArray(1024 * 8)

            do {
                val read = it.read(buffer)

                if (read != -1) digest.update(buffer, 0, read)
            } while (read != -1)
        }
        return digest.digest().toHexString()
    }

}