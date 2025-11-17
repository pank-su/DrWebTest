package su.pank.drwebtest.data.apps

import android.content.pm.ApplicationInfo
import kotlinx.coroutines.flow.Flow
import su.pank.drwebtest.data.model.App

/**
 * Получение списка приложений
 */
interface AppsRepository {

    val apps: Flow<List<App>>
}