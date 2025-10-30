@file:OptIn(ExperimentalMaterial3Api::class)

package es.javiercarrasco.ejemplost2

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import es.javiercarrasco.ejemplost2.MainActivity.Companion.usuarios
import es.javiercarrasco.ejemplost2.ui.theme.EjemplosT21Theme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    companion object {
        val itemsList = List(100) { "Item #$it" }
        val dias = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
        val frutas = listOf(
            "Manzana", "Pera", "Naranja", "Plátano", "Fresa",
            "Kiwi", "Mango", "Piña", "Uva", "Sandía"
        )
        val usuarios = listOf(
            "Ana", "Luis", "Carlos", "Lucía", "María", "Javier",
            "Patricia", "Sofía", "Pedro", "Laura", "David", "Isabel"
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EjemplosT21Theme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MiTarjeta(usuario: String, onClick: (String) -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick(usuario)
            }
            .padding(4.dp)
    ) {
        Row {
            Image(
                painter = painterResource(id = R.drawable.ic_contact),
                contentDescription = "Imagen de usuario",
                modifier = Modifier
                    .wrapContentSize()
                    .size(50.dp)
            )
            Text(
                text = usuario,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview
@Composable
fun MainScreen() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { TopAppBar({ Text(stringResource(R.string.app_name)) }) },
        floatingActionButton = {}
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = usuarios, key = { it }) { usuario ->
                MiTarjeta(
                    usuario
                ) { usu ->
                    scope.launch {
                        val resultado = snackbarHostState.showSnackbar(
                            message = "Usuario seleccionado: $usuario",
                            actionLabel = "Deshacer",
                            duration = SnackbarDuration.Short
                        )
                        if (resultado == SnackbarResult.ActionPerformed) {
                            Log.i("SNACKBAR", "El usuario pulsó deshacer!")
                        }
                    }
                }
            }
        }
    }
}
