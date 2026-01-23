package es.javiercarrasco.ejemplologin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import es.javiercarrasco.ejemplologin.ui.screens.HomeScreen
import es.javiercarrasco.ejemplologin.ui.theme.EjemploLoginTheme

/**
 * Main Activity
 * @author Javier Carrasco
 * Ejemplo de login con token en Jetpack Compose.
 * Usuario: alumno
 * Password: alumno
 */

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EjemploLoginTheme {
                Scaffold(
                    topBar = {
                        TopAppBar({ Text(text = stringResource(R.string.app_name)) })
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center,
                        content = { HomeScreen() }
                    )
                }
            }
        }
    }
}
