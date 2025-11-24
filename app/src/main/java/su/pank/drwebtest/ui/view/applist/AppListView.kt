package su.pank.drwebtest.ui.view.applist

import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import su.pank.drwebtest.R
import su.pank.drwebtest.data.model.App
import su.pank.drwebtest.ui.components.AppItem
import kotlin.time.Duration.Companion.seconds

@Serializable
object AppList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppList(onSelectApp: (App) -> Unit, viewModel: AppListViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
        TopAppBar({ Text("Установленные приложения") }, scrollBehavior = scrollBehavior)
    }) {
        Box(modifier = Modifier.padding(it)) {
            when (state) {
                is AppListState.Loading -> {
                    LoadingAppList()
                }

                AppListState.Error -> {
                    Text("Error")
                }

                is AppListState.Success -> {
                    SuccessAppList((state as AppListState.Success).apps, onSelectApp, viewModel)
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SuccessAppList(
    apps: List<App>,
    onSelectApp: (App) -> Unit,
    viewModel: AppListViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val packageManager = context.packageManager

    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(apps, key = { it.packageName }) { app ->

            var hash: String? by remember {
                mutableStateOf(null)
            }
            LaunchedEffect(app) {
                //delay(0.5.seconds) // Небольшая задержка, чтобы не нагружать сразу базу данных при большом количестве приложений
                hash = viewModel.getHashForApp(app.packageName)
            }
            AppItem(app.name, app.version, app.packageName, hash, {
                onSelectApp(app)
            }, modifier = Modifier.padding(horizontal = 16.dp))
        }
    }

}


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LoadingAppList() {
    val labels = remember {
        listOf(
            "Загрузка приложений...",
            "Проверка установленных программ...",
            "Получение списка приложений...",
            "Подготовка данных...",
            "Почти готово...",
            "Секундочку...",
            "Ещё чуть-чуть...",
        )
    }
    var selectedLabelString by remember {
        mutableStateOf(labels.random())
    }
    LaunchedEffect(null) {
        while (true) {
            delay(1.5.seconds)
            selectedLabelString = labels.random()
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            LoadingIndicator()
            Spacer(Modifier.height(10.dp))
            AnimatedContent(selectedLabelString) {
                Text(
                    it,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.widthIn(max = 200.dp),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@Composable
fun ErrorAppList() {
    val activity = LocalActivity.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.error
    ) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                10.dp,
                alignment = Alignment.CenterVertically
            )
        ) {

            Icon(
                painterResource(R.drawable.error),
                contentDescription = "Error icon",
                modifier = Modifier.size(48.dp)
            )
            Text("Ошибка загрузки приложений", style = MaterialTheme.typography.headlineSmall)
            Button(
                onClick = {
                    activity?.finish()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text("Выйти из приложения")
            }
        }

    }

}

@Preview
@Composable
private fun LoadingAppListPreview() {
    LoadingAppList()
}


@Preview
@Composable
private fun ErrorAppListPreview() {
    ErrorAppList()
}
