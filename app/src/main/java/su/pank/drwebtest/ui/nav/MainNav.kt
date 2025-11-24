package su.pank.drwebtest.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import su.pank.drwebtest.ui.view.appdetail.AppDetail
import su.pank.drwebtest.ui.view.applist.AppList

@Composable
fun MainNav(){
    val navController = rememberNavController()

    NavHost(navController, startDestination = AppList){
        composable<AppList>{
            AppList(onSelectApp = { app ->
                navController.navigate(
                    AppDetail(
                        packageName = app.packageName,
                        name = app.name,
                        version = app.version,
                    )
                )
            })
        }

        composable<AppDetail> {
            val appDetail = it.toRoute<AppDetail>()
            AppDetail(
                app = appDetail,
                onBack = { navController.navigateUp() }
            )
        }
    }
}