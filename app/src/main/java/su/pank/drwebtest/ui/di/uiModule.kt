package su.pank.drwebtest.ui.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import su.pank.drwebtest.ui.view.AppListViewModel

val uiModule = module{
    viewModelOf(::AppListViewModel)
}