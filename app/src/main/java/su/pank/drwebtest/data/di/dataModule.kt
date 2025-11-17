package su.pank.drwebtest.data.di

import org.koin.dsl.module
import su.pank.drwebtest.data.apps.AppsRepository
import su.pank.drwebtest.data.apps.DefaultAppsRepository

val dataModule = module {
    single<AppsRepository> {
        DefaultAppsRepository(get())
    }
}