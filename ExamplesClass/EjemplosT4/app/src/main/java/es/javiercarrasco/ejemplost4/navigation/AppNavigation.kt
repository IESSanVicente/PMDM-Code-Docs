package es.javiercarrasco.ejemplost4.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import es.javiercarrasco.ejemplost4.SharedViewModel
import es.javiercarrasco.ejemplost4.ui.screens.DetailScreen
import es.javiercarrasco.ejemplost4.ui.screens.MainScreen
import es.javiercarrasco.ejemplost4.ui.screens.ThirdScreen

@Composable
fun AppNavigation(modifier: Modifier) {
    val navController: NavHostController = rememberNavController()
    val sharedViewModel: SharedViewModel = viewModel()

    NavHost(navController = navController, startDestination = MyRoutes.MyHomeScreen.route) {
        composable(MyRoutes.MyHomeScreen.route) {
            MainScreen(modifier, navController)
        }
        composable(MyRoutes.MyDetailScreen.route) {
            DetailScreen(modifier, navController, sharedViewModel)
        }
        composable(MyRoutes.MyThirdScreen.route) {backStackEntry ->
//            val datos = backStackEntry.arguments?.getString("datos")
//            datos?.let {
                ThirdScreen(modifier, navController, sharedViewModel)
//            }
        }
    }
}