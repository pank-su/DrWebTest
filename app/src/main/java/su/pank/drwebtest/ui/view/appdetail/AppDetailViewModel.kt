package su.pank.drwebtest.ui.view.appdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import su.pank.drwebtest.data.apps.AppsRepository
import su.pank.drwebtest.data.model.AppDetailedInfo

class AppDetailViewModel(
    private val stateHandle: SavedStateHandle,
    private val appsRepository: AppsRepository
) : ViewModel() {

    val packageName: String = stateHandle["packageName"] ?: error("packageName is required")

    val detailedInfo: StateFlow<AppDetailedInfoState> = flow<AppDetailedInfoState> {
        val info = appsRepository.getAppDetailedInfo(packageName)
        if (info != null) {
            emit(AppDetailedInfoState.Success(info))
        } else {
            emit(AppDetailedInfoState.Error)
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, AppDetailedInfoState.Loading)


}

sealed interface AppDetailedInfoState {
    object Loading : AppDetailedInfoState
    data class Success(val info: AppDetailedInfo) : AppDetailedInfoState
    object Error : AppDetailedInfoState
}
