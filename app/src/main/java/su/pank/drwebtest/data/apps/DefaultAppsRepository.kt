package su.pank.drwebtest.data.apps

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import su.pank.drwebtest.data.model.App
import java.security.MessageDigest

class DefaultAppsRepository(private val context: Context) : AppsRepository {
    private val pm = context.packageManager
    override val apps: Flow<List<App>> = flow {
        pm.getInstalledApplications(0).map { info ->
            App(
                name = pm.getApplicationLabel(info).toString(),
                version = pm.getPackageInfo(info.packageName, 0).versionName
                    ?: "N/A",
                packageName = info.packageName,
                hashSum = getAppSignatureHash(info.packageName) ?: "N/A"
            )
        }.sortedBy { it.name }.also({ emit(it) })
    }

    private fun getAppSignatureHash(packageName: String, algorithm: String = "SHA-256"): String? {
        val packageInfo = pm.getPackageInfo(
            packageName,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) PackageManager.GET_SIGNING_CERTIFICATES else PackageManager.GET_SIGNATURES
        )
        val signatures = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.signingInfo?.apkContentsSigners ?: emptyArray()
        } else {
            packageInfo.signatures ?: emptyArray()
        }

        if (signatures.isEmpty()) return null


        val signatureBytes = signatures[0].toByteArray()

        val digest = MessageDigest.getInstance(algorithm)
        val hashBytes = digest.digest(signatureBytes)

        return hashBytes.toHexString()
    }
}