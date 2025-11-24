package su.pank.drwebtest.data.model

data class AppDetailedInfo(
    val packageName: String,
    val sourceDir: String,
    val dataDir: String,
    val targetSdkVersion: Int,
    val minSdkVersion: Int,
    val installTime: Long,
    val updateTime: Long,
    val versionCode: Long,
    val isSystemApp: Boolean,
    val permissions: List<String>,
    val apkSize: Long,
    val hash: String? = null
)
