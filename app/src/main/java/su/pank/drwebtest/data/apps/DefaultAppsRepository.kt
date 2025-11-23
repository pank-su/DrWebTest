package su.pank.drwebtest.data.apps

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import su.pank.drwebtest.data.model.App
import su.pank.drwebtest.data.model.AppDetailedInfo
import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest

class DefaultAppsRepository(
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
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

    override suspend fun getAppDetailedInfo(packageName: String): AppDetailedInfo? =
        withContext(ioDispatcher) {
            try {
                val packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
                val appInfo = packageInfo.applicationInfo!!

                val permissions = packageInfo.requestedPermissions?.toList() ?: emptyList()
                val apkFile = File(appInfo.sourceDir)

                AppDetailedInfo(
                    packageName = packageName,
                    sourceDir = appInfo.sourceDir,
                    dataDir = appInfo.dataDir,
                    targetSdkVersion = appInfo.targetSdkVersion,
                    minSdkVersion = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        appInfo.minSdkVersion
                    } else {
                        0
                    },
                    installTime = packageInfo.firstInstallTime,
                    updateTime = packageInfo.lastUpdateTime,
                    versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        packageInfo.longVersionCode
                    } else {
                        @Suppress("DEPRECATION")
                        packageInfo.versionCode.toLong()
                    },
                    isSystemApp = (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0,
                    permissions = permissions,
                    apkSize = apkFile.length()
                )
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

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