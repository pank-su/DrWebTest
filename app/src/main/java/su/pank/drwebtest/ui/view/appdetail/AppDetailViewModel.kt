package su.pank.drwebtest.ui.view.appdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import su.pank.drwebtest.data.apps.AppsRepository
import su.pank.drwebtest.data.model.AppDetailedInfo

class AppDetailViewModel(private val appsRepository: AppsRepository) : ViewModel() {
    private val _detailedInfo = MutableStateFlow<AppDetailedInfoState>(AppDetailedInfoState.Loading)
    val detailedInfo: StateFlow<AppDetailedInfoState> = _detailedInfo.asStateFlow()

    fun loadDetailedInfo(packageName: String) {
        viewModelScope.launch {
            _detailedInfo.value = AppDetailedInfoState.Loading
            val info = appsRepository.getAppDetailedInfo(packageName)
            _detailedInfo.value = if (info != null) {
                AppDetailedInfoState.Success(info)
            } else {
                AppDetailedInfoState.Error
            }
        }
    }
}

sealed interface AppDetailedInfoState {
    object Loading : AppDetailedInfoState
    data class Success(val info: AppDetailedInfo) : AppDetailedInfoState
    object Error : AppDetailedInfoState
}
