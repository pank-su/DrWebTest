package su.pank.drwebtest.data.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import su.pank.drwebtest.data.apps.AppsRepository
import su.pank.drwebtest.data.apps.DefaultAppsRepository
import su.pank.drwebtest.data.hash.HashRepository
import su.pank.drwebtest.data.hash.LocalHashRepository

val dataModule = module {
    single<AppsRepository> {
        DefaultAppsRepository(get())
    }
    single<HashRepository>{
        LocalHashRepository(get(), get(), get())
    }
    single<CoroutineDispatcher>{
        Dispatchers.IO
    }
}