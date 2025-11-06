@file:OptIn(ExperimentalMaterial3Api::class)

package es.javiercarrasco.ejemplost2

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import es.javiercarrasco.ejemplost2.MainActivity.Companion.usuarios
import es.javiercarrasco.ejemplost2.ui.theme.EjemplosT21Theme
import kotlinx.coroutines.launch
import me.saket.cascade.CascadeDropdownMenu

class MainActivity : ComponentActivity() {
    companion object {
        var usuarios = mutableListOf(
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
    val context = LocalContext.current

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
                    .weight(.25f)
            )
            Text(
                text = usuario,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .weight(.5f),
                style = MaterialTheme.typography.bodyLarge
            )
            MyMenuContextual(usuario)
        }
    }
}

@Composable
fun MyMenu(onOption1: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = !expanded }) {
        Icon(Icons.Outlined.MoreVert, contentDescription = "Menu Opciones")
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        DropdownMenuItem(
            trailingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            text = { Text("Opción 1") },
            onClick = {
                /* Nuestra lógica */
                onOption1()
                expanded = false
            }
        )
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 8.dp), // Espacio horizontal (opcional).
            thickness = 1.dp, // Espacio entre los elementos del menú (opcional).
            color = Color.Red // Color del divisor (opcional).
        )
        DropdownMenuItem(
            text = { Text("Opción 2") },
            onClick = {
                /* Nuestra lógica */
                expanded = false
            }
        )
    }
}


@Composable
fun MyMenuContextual(name: String) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    IconButton(onClick = { expanded = !expanded }) {
        Icon(Icons.Outlined.Face, contentDescription = "Menu Opciones")
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        DropdownMenuItem(
            text = { Text("Saluda") },
            onClick = {
                /* Nuestra lógica */
                Toast.makeText(context, "Hola $name!", Toast.LENGTH_SHORT).show()
                expanded = false
            }
        )
    }
}

@Preview
@Composable
fun MainScreen() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    MyMenu(
                        onOption1 = {
//                            Toast.makeText(context, "Opción 1 seleccionada", Toast.LENGTH_SHORT)
//                                .show()
                            usuarios.clear()
                        }
                    )
                })
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            CascadeMenu()
        }
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues),
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            items(items = usuarios, key = { it }) { usuario ->
//                MiTarjeta(
//                    usuario
//                ) { usu ->
//                    scope.launch {
//                        val resultado = snackbarHostState.showSnackbar(
//                            message = "Usuario seleccionado: $usu",
//                            duration = SnackbarDuration.Short
//                        )
//                        if (resultado == SnackbarResult.ActionPerformed) {
//                            Log.i("SNACKBAR", "El usuario pulsó deshacer!")
//                        }
//                    }
//                }
//            }
//        }
    }
}


//@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuSpinner() {
    val opciones = listOf("Rojo", "Verde", "Azul", "Amarillo")
    var expanded by remember { mutableStateOf(false) }
    var seleccion by remember { mutableStateOf(opciones[0]) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = seleccion,
            onValueChange = {},
            readOnly = true,
            label = { Text("Color favorito") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryEditable, true)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            opciones.forEach { color ->
                DropdownMenuItem(
                    text = { Text(color) },
                    onClick = {
                        seleccion = color
                        expanded = false
                    }
                )
            }
        }
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpinnerAutocomplete() {
    val opciones = listOf("Alicante", "Barcelona", "Bilbao", "Madrid", "Valencia", "Zaragoza")
    var expanded by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }
    val filtradas = opciones.filter { it.contains(text, true) }

    ExposedDropdownMenuBox(
        expanded = expanded && filtradas.isNotEmpty(),
        onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
                expanded = true
            },
            singleLine = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            label = { Text("Ciudad") },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(
                    MenuAnchorType.PrimaryEditable,
                    true
                ) // Ancla el menú al campo de texto.
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            filtradas.forEach { ciudad ->
                DropdownMenuItem(text = { Text(ciudad) }, onClick = {
                    text = ciudad
                    expanded = false
                })
            }
        }
    }
}

@Composable
fun CascadeMenu() {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(Icons.Default.MoreVert, contentDescription = "Menú cascada")
        }

        CascadeDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            // Item principal con submenú
            DropdownMenuItem(
                text = { Text("Opciones Avanzadas ▸") },
                children = {
                    DropdownMenuItem(
                        text = { Text("Sub‑opción 1") },
                        onClick = {
                            expanded = false
                            Toast.makeText(context, "Sub‑opción 1", Toast.LENGTH_SHORT).show()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Sub‑opción 2") },
                        onClick = {
                            expanded = false
                            Toast.makeText(context, "Sub‑opción 2", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            )
            // Otro item principal
            DropdownMenuItem(
                text = { Text("Acerca de") },
                onClick = {
                    expanded = false
                    Toast.makeText(context, "Acerca de", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}