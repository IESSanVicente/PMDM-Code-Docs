package es.javiercarrasco.ejemplost4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import es.javiercarrasco.ejemplost4.navigation.AppNavigation
import es.javiercarrasco.ejemplost4.ui.theme.EjemplosT4Theme

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EjemplosT4Theme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(title = { Text(stringResource(R.string.app_name)) })
                    }
                ) { innerPadding ->
//                    MainScreen(modifier = Modifier.padding(innerPadding))
                    AppNavigation(Modifier.padding(innerPadding))
                }
            }
        }
    }
}


















