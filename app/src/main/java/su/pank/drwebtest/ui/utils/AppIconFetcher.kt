package su.pank.drwebtest.ui.utils

import android.content.Context
import android.content.pm.PackageManager
import coil3.ImageLoader
import coil3.decode.DataSource
import coil3.fetch.FetchResult
import coil3.fetch.Fetcher
import coil3.fetch.ImageFetchResult
import coil3.request.Options
import coil3.asImage

data class AppIcon(val packageName: String)

class AppIconFetcher(
    private val data: AppIcon,
    private val options: Options,
    private val context: Context
) : Fetcher {

    override suspend fun fetch(): FetchResult? {
        return try {
            val drawable = context.packageManager.getApplicationIcon(data.packageName)
            ImageFetchResult(
                image = drawable.asImage(),
                isSampled = false,
                dataSource = DataSource.DISK
            )
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }

    class Factory(private val context: Context) : Fetcher.Factory<AppIcon> {
        override fun create(
            data: AppIcon,
            options: Options,
            imageLoader: ImageLoader
        ): Fetcher {
            return AppIconFetcher(data, options, context)
        }
    }
}
