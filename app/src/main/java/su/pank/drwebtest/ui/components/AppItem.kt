package su.pank.drwebtest.ui.components

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import su.pank.drwebtest.R


// Разделение на параметров для уменьшения количества рекомпозиций при изменении данных
@Composable
fun AppItem(
    icon: Drawable?,
    name: String,
    version: String,
    packageName: String,
    hashSum: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

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
            if (icon != null)
                Image(
                    icon.toBitmap().asImageBitmap(),
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

                Text(
                    text = hashSum,

                    style = MaterialTheme.typography.bodyMedium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.widthIn(max = 70.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun AppItemPreview() {
    AppItem(
        ResourcesCompat.getDrawable(
            LocalResources.current,
            R.drawable.ic_launcher_foreground,
            null
        )!!,
        name = "Example App",
        version = "1.0.0",
        packageName = "com.example.app",
        hashSum = "abc123def456",
        {}
    )
}