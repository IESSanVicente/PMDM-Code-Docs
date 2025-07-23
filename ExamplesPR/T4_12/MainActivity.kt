package es.javiercarrasco.examplet4_12

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import es.javiercarrasco.examplet4_12.ui.theme.ExampleT4_12Theme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExampleT4_12Theme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(text = getString(R.string.app_name))
                            },
                            colors = topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary,
                            )
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Column(
                        modifier = Modifier.fillMaxSize().padding(innerPadding)
                    ) {
                        MyAppNav()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyAppNav() {
    val navController: NavHostController = rememberNavController()

    NavHost(navController, startDestination = "home") {
        composable("home") { Home(navController) }
        composable("detail") { Detail(navController) }
        composable("config") { Config(navController) }
    }
}

@Composable
fun Home(navController: NavHostController) {
    Column (
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text(
            text = "Home Screen",
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        Button(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            onClick = { navController.navigate("detail") }) { Text("Go to Detail") }
    }
}

@Composable
fun Detail(navController: NavHostController) {
    Column (
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text(
            text = "Detail Screen",
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        Button(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            onClick = { navController.navigate("config") }) { Text("Go to Configuration") }
    }
}

@Composable
fun Config(navController: NavHostController) {
    Column (
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text(
            text = "Configuration Screen",
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        Button(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            onClick = {
                navController.navigate("home") {
                    popUpTo("home") // Elimina hasta "home"
                    launchSingleTop = true // Evita duplicados
                }
            }) { Text("Go to Home") }
    }
}
