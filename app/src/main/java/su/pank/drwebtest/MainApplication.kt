package su.pank.drwebtest

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import su.pank.drwebtest.data.di.dataModule
import su.pank.drwebtest.database.di.dataBaseModule
import su.pank.drwebtest.domain.di.domainModule
import su.pank.drwebtest.ui.di.uiModule
import su.pank.drwebtest.ui.utils.AppIconFetcher

class MainApplication: Application(), SingletonImageLoader.Factory {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(uiModule, dataModule, dataBaseModule, domainModule)
        }
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader = ImageLoader.Builder(context)
        .components {
            add(AppIconFetcher.Factory(context))
        }
        .build()
}