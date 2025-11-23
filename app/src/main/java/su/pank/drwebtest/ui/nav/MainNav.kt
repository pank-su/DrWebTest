package su.pank.drwebtest.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import su.pank.drwebtest.ui.view.applist.AppList

@Composable
fun MainNav(){
    val navController = rememberNavController()

    NavHost(navController, startDestination = AppList){
        composable<AppList>{
            AppList(onSelectApp = {

            })
        }

    }
}