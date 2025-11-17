package su.pank.drwebtest.ui.view

import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import su.pank.drwebtest.data.model.App
import su.pank.drwebtest.ui.components.AppItem

@Composable
fun AppList(viewModel: AppListViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (state) {
        is AppListState.Loading -> {
            Text("Loading")
        }

        AppListState.Error -> {
            Text("Error")
        }

        is AppListState.Success -> {
            SuccessAppList((state as AppListState.Success).apps)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SuccessAppList(apps: List<App>) {
    val context = LocalContext.current
    val packageManager = context.packageManager
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())


    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
        TopAppBar({ Text("Установленные приложения") }, scrollBehavior = scrollBehavior)
    }) {

        LazyColumn(modifier = Modifier.padding(it), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(apps, key = { it.packageName }) { app ->
                val icon = remember {
                    packageManager.getApplicationIcon(app.packageName)
                }
                AppItem(icon, app.name, app.version, app.packageName, app.hashSum)
            }
        }
    }
}