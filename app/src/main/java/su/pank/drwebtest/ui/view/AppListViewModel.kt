package su.pank.drwebtest.ui.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import su.pank.drwebtest.data.apps.AppsRepository
import su.pank.drwebtest.data.model.App
import kotlin.time.Duration.Companion.seconds

class AppListViewModel(private val appsRepository: AppsRepository) : ViewModel() {
    val state =
        appsRepository.apps.map { AppListState.Success(it) }.catch { AppListState.Error }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            AppListState.Loading
        )
}

sealed interface AppListState {
    object Loading : AppListState
    data class Success(val apps: List<App>) : AppListState
    object Error : AppListState
}