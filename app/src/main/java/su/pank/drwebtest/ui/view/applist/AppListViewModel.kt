package su.pank.drwebtest.ui.view.applist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import su.pank.drwebtest.data.apps.AppsRepository
import su.pank.drwebtest.data.model.App
import su.pank.drwebtest.domain.ApkHashUseCase

class AppListViewModel(private val appsRepository: AppsRepository, private val apkHashUseCase: ApkHashUseCase) : ViewModel() {
    val state: StateFlow<AppListState> =
        appsRepository.apps.map { AppListState.Success(it) }.catch<AppListState> {
            it.printStackTrace()

            emit(AppListState.Error)
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            AppListState.Loading
        )

    suspend fun getHashForApp(packageName: String): String? {
        return apkHashUseCase(packageName)
    }
}

sealed interface AppListState {
    object Loading : AppListState
    data class Success(val apps: List<App>) : AppListState
    object Error : AppListState
}