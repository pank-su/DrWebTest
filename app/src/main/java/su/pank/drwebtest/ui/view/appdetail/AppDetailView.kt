package su.pank.drwebtest.ui.view.appdetail

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import su.pank.drwebtest.data.model.AppDetailedInfo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Serializable
data class AppDetail(
    val packageName: String,
    val name: String,
    val version: String,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDetail(
    app: AppDetail,
    onBack: () -> Unit,
    viewModel: AppDetailViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val packageManager = context.packageManager
    
    val detailedInfoState by viewModel.detailedInfo.collectAsStateWithLifecycle()
    


    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val icon = remember {
        try {
            packageManager.getApplicationIcon(app.packageName)
        } catch (_: Exception) {
            null
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text("Детали приложения") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            icon?.let {
                Image(
                    it.toBitmap().asImageBitmap(),
                    contentDescription = "Иконка приложения",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(24.dp))
                )
            }

            Text(
                text = app.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InfoRow(label = "Пакет", value = app.packageName)
                    InfoRow(label = "Версия", value = app.version)
                    if (detailedInfoState is AppDetailedInfoState.Success) {
                        InfoRow(
                            label = "Хеш-сумма",
                            value = (detailedInfoState as AppDetailedInfoState.Success).info.hash
                                ?: "N/A"
                        )
                    } else {
                        InfoRow(
                            label = "Хеш-сумма",
                            value = "Загрузка..."
                        )
                    }                }
            }

            DetailedInfoSection(detailedInfoState)

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val launchIntent = packageManager.getLaunchIntentForPackage(app.packageName)
                    if (launchIntent != null) {
                        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(launchIntent)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Запустить приложение")
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outline
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun DetailedInfoSection(state: AppDetailedInfoState) {
    var isExpanded by remember { mutableStateOf(false) }
    
    OutlinedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            // Заголовок с кнопкой раскрытия
            TextButton(
                onClick = { isExpanded = !isExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Дополнительная информация",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Icon(
                        if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (isExpanded) "Свернуть" else "Развернуть"
                    )
                }
            }
            
            // Содержимое
            AnimatedVisibility(visible = isExpanded) {
                when (state) {
                    is AppDetailedInfoState.Loading -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Загрузка информации...")
                        }
                    }
                    
                    is AppDetailedInfoState.Success -> {
                        DetailedInfoContent(state.info)
                    }
                    
                    is AppDetailedInfoState.Error -> {
                        Text(
                            "Не удалось загрузить дополнительную информацию",
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailedInfoContent(info: AppDetailedInfo) {
    val dateFormat = remember { SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()) }
    
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        InfoRow(label = "Код версии", value = info.versionCode.toString())
        InfoRow(label = "Целевой SDK", value = info.targetSdkVersion.toString())
        InfoRow(label = "Минимальный SDK", value = if (info.minSdkVersion > 0) info.minSdkVersion.toString() else "N/A")
        InfoRow(label = "Системное приложение", value = if (info.isSystemApp) "Да" else "Нет")
        InfoRow(label = "Путь к APK", value = info.sourceDir)
        InfoRow(label = "Путь к данным", value = info.dataDir)
        InfoRow(label = "Размер APK", value = formatFileSize(info.apkSize))
        InfoRow(label = "Установлено", value = dateFormat.format(Date(info.installTime)))
        InfoRow(label = "Обновлено", value = dateFormat.format(Date(info.updateTime)))
        InfoRow(label = "Количество разрешений", value = info.permissions.size.toString())
        
        if (info.permissions.isNotEmpty()) {
            var showPermissions by remember { mutableStateOf(false) }
            
            TextButton(onClick = { showPermissions = !showPermissions }) {
                Text(if (showPermissions) "Скрыть разрешения" else "Показать разрешения")
            }
            
            if (showPermissions) {
                Column(
                    modifier = Modifier.padding(start = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    info.permissions.forEach { permission ->
                        Text(
                            text = "• ${permission.substringAfterLast('.')}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }
    }
}

private fun formatFileSize(size: Long): String {
    val kb = size / 1024.0
    val mb = kb / 1024.0
    val gb = mb / 1024.0
    
    return when {
        gb >= 1 -> String.format("%.2f ГБ", gb)
        mb >= 1 -> String.format("%.2f МБ", mb)
        kb >= 1 -> String.format("%.2f КБ", kb)
        else -> "$size Б"
    }
}
