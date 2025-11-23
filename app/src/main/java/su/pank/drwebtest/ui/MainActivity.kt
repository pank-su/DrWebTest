package su.pank.drwebtest.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import su.pank.drwebtest.ui.theme.DrWebTestTheme
import su.pank.drwebtest.ui.view.applist.AppList

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DrWebTestTheme {
                AppList()

            }
        }
    }
}