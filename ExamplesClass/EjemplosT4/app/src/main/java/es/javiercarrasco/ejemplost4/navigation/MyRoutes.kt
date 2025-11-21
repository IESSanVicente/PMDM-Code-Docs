package es.javiercarrasco.ejemplost4.navigation

sealed class MyRoutes(val route: String) {
    object MyHomeScreen : MyRoutes("home")
    object MyDetailScreen: MyRoutes("detail")
    object MyThirdScreen: MyRoutes("third") {
        fun createRoute(datos: String) = "third/$datos"
    }
}