package su.pank.drwebtest.database.di

import android.content.Context
import androidx.room.Room
import org.koin.dsl.module
import su.pank.drwebtest.database.HashDataBase

val dataBaseModule = module {
    single<HashDataBase> {
        Room.databaseBuilder(get<Context>(), HashDataBase::class.java, "hash_db").build()
    }
    single {
        get<HashDataBase>().hashDao()
    }
}