package su.pank.drwebtest.domain.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import su.pank.drwebtest.domain.ApkHashUseCase

val domainModule = module {
    singleOf(::ApkHashUseCase)
}