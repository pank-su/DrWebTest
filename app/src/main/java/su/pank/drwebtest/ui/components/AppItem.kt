package su.pank.drwebtest.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import su.pank.drwebtest.ui.utils.AppIcon


// Разделение на параметров для уменьшения количества рекомпозиций при изменении данных
@Composable
fun AppItem(
    name: String,
    version: String,
    packageName: String,
    hashSum: String? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    ElevatedCard(
        onClick = onClick,
        modifier
            .widthIn(max = 600.dp),
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            AsyncImage(
                ImageRequest.Builder(LocalContext.current)
                    .data(AppIcon(packageName))
                    .memoryCacheKey(packageName).diskCacheKey(packageName)
                    .crossfade(true)
                    .build(),
                contentDescription = "App icon",
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .size(50.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.basicMarquee()
                )
                Text(
                    text = packageName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = version,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.widthIn(max = 70.dp)
                )

                if (hashSum != null)
                    Text(
                        text = hashSum,
                        style = MaterialTheme.typography.bodyMedium,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        modifier = Modifier.widthIn(max = 70.dp)
                    )
                else
                    Box(
                        Modifier
                            .background(
                                MaterialTheme.colorScheme.outline,
                                RoundedCornerShape(2.dp)
                            )
                            .height(14.dp)
                            .width(70.dp)
                    )
            }
        }
    }
}

@Preview
@Composable
fun AppItemPreview() {
    AppItem(
        name = "Example App",
        version = "1.0.0",
        packageName = "com.example.app",
        hashSum = "abc123def456",
        {}
    )
}