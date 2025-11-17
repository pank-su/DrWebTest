package su.pank.drwebtest

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import su.pank.drwebtest.data.di.dataModule
import su.pank.drwebtest.ui.di.uiModule

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(uiModule, dataModule)
        }
    }
}