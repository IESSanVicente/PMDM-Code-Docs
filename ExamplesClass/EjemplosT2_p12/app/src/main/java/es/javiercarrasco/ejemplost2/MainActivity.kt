@file:OptIn(ExperimentalMaterial3Api::class)

package es.javiercarrasco.ejemplost2

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import es.javiercarrasco.ejemplost2.ui.components.AlertDialogExample
import es.javiercarrasco.ejemplost2.ui.components.BasicDialog
import es.javiercarrasco.ejemplost2.ui.components.CustomDialog
import es.javiercarrasco.ejemplost2.ui.components.DialogoSeleccionFecha
import es.javiercarrasco.ejemplost2.ui.components.DialogoSeleccionHora
import es.javiercarrasco.ejemplost2.ui.components.MiTarjeta
import es.javiercarrasco.ejemplost2.ui.theme.EjemplosT21Theme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
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
fun MyMenu(onClickOption: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = !expanded }) {
        Icon(Icons.Rounded.MoreVert, contentDescription = "Menu Opciones")
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        DropdownMenuItem(
            leadingIcon = { Icon(Icons.Rounded.Info, contentDescription = null) },
            text = { Text("Opción 1") },
            onClick = {
                /* Nuestra lógica */
                onClickOption()
                expanded = false
            }
        )
    }
}

@Preview
@Composable
fun MainScreen() {
    val usuarios = rememberSaveable {
        mutableStateListOf(
            "Ana", "Luis", "Carlos", "Lucía", "María", "Javier",
            "Patricia", "Sofía", "Pedro", "Laura", "David", "Isabel"
        )
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val openInfoDialog = remember { mutableStateOf(false) }
    val openAlertDialog = remember { mutableStateOf(false) }

    val openInfoDialogContextual = remember { mutableStateOf(false) }
    val selectedName = remember { mutableStateOf("") }

    val basicDialog: @Composable (String) -> Unit = { user ->
        BasicDialog(
            user, onDismissRequest = { openInfoDialogContextual.value = false },
            onConfirmation = {
                openInfoDialogContextual.value = false
                println("Confirmación recibida")
            })
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    MyMenu(onClickOption = { openAlertDialog.value = true })
                })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                modifier = Modifier
                    .padding(8.dp)
                    .height(40.dp),
                onClick = { openInfoDialog.value = true }) {
                Text("Mostrar info")
            }
            if (openInfoDialog.value) {
                AlertDialog(
                    onDismissRequest = {
                        openInfoDialog.value = false
                    }, // Se cierra el diálogo al tocar fuera de él.
                    title = { Text("Información") },
                    text = { Text("Esta es una notificación informativa.") },
                    confirmButton = {
                        TextButton(onClick = {
                            // Más acciones si es necesario al confirmar.
                            openInfoDialog.value = false
                        }) {
                            Text(LocalContext.current.getString(android.R.string.ok))
                        }
                    }
                )
            }

            if (openAlertDialog.value) {
                AlertDialogExample(
                    onDismissRequest = { openAlertDialog.value = false },
                    onConfirmation = {
                        openAlertDialog.value = false
                        println("Confirmación recibida")
                    },
                    dialogTitle = "Título del Diálogo",
                    dialogText = "Este es un ejemplo de cuadro de diálogo en Jetpack Compose.",
                    icon = Icons.Default.Info
                )
            }

            if (openInfoDialogContextual.value) {
                BasicDialog(
                    selectedName.value,
                    onDismissRequest = { openInfoDialogContextual.value = false },
                    onConfirmation = {
                        openInfoDialogContextual.value = false
                        println("Confirmación recibida")
                    })
                //basicDialog(selectedName.value)
            }

            CustomDialog { name ->
                usuarios.add(name)
            }

            DialogoSeleccionHora()

            DialogoSeleccionFecha()

            // Listado de usuarios.
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = usuarios, key = { it }) { usuario ->
                    MiTarjeta(usuario, onClick = { usu ->
                        scope.launch {
                            val resultado = snackbarHostState.showSnackbar(
                                message = "Usuario seleccionado: $usu",
                                duration = SnackbarDuration.Short
                            )
                            if (resultado == SnackbarResult.ActionPerformed) {
                                Log.i("SNACKBAR", "El usuario pulsó deshacer!")
                            }
                        }
                    }, onClickInfo = {
                        selectedName.value = it
                        openInfoDialogContextual.value = true
                    })
                }
            }
        }
    }
}

