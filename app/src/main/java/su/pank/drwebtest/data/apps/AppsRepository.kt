package su.pank.drwebtest.data.apps

import android.content.pm.ApplicationInfo
import kotlinx.coroutines.flow.Flow
import su.pank.drwebtest.data.model.App
import su.pank.drwebtest.data.model.AppDetailedInfo

/**
 * Получение списка приложений
 */
interface AppsRepository {

    val apps: Flow<List<App>>
    
    suspend fun getAppDetailedInfo(packageName: String): AppDetailedInfo?
}