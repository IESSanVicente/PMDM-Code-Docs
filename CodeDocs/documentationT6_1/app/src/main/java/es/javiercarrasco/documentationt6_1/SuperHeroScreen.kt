package es.javiercarrasco.documentationt6_1

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import es.javiercarrasco.documentationt6_1.model.Editorial
import es.javiercarrasco.documentationt6_1.model.SuperHero

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperHeroScreen(navController: NavController, viewModel: SupersViewModel, idSuper: Int = 0) {
    var superName by remember { mutableStateOf("") }
    var realName by remember { mutableStateOf("") }
    var editorial by remember { mutableStateOf(0) }
    val currentEditorials by viewModel.currentEditorials.collectAsStateWithLifecycle()
    var favorito by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    var buttonEnabled by remember { mutableStateOf(true) }

    var oneSuper = SuperHero(
        idSuper = 0,
        superName = "",
        realName = "",
        favorite = false,
        idEditorial = 0
    )

    if (idSuper != 0) {
        LaunchedEffect(Unit) {
            val superById = viewModel.getSuperById(idSuper).await()
            if (superById != null) {
                oneSuper = superById.copy(
                    idSuper = superById.idSuper,
                    superName = superById.superName,
                    realName = superById.realName,
                    favorite = superById.favorite,
                    idEditorial = superById.idEditorial
                )

                superName = superById.superName
                realName = superById.realName
                favorito = superById.favorite
                editorial = superById.idEditorial
            } else {
                // Manejo de error si no se encuentra el superhéroe.
                println("No se encontró el superhéroe con ID: $idSuper")
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Añadir superhéroe") }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = superName,
                singleLine = true, // Permite una sola línea de texto, evita el salto de línea y añade navegación con el tabulador
                onValueChange = {
                    superName = it
                    isError = it.isEmpty() // Actualiza el estado de error al cambiar el texto.
                },
                label = { Text("Nombre de superhéroe") },
                modifier = Modifier.fillMaxWidth(.95F),
                isError = isError,
                supportingText = {
                    if (isError) {
                        Text("El nombre del superhéroe no puede estar vacío.")
                    }
                }
            )

            OutlinedTextField(
                value = realName,
                singleLine = true,
                onValueChange = {
                    realName = it
                    isError = it.isEmpty() // Actualiza el estado de error al cambiar el texto.
                },
                label = { Text("Nombre real del superhéroe") },
                modifier = Modifier.fillMaxWidth(.95F),
                isError = isError,
                supportingText = {
                    if (isError) {
                        Text("El nombre real del superhéroe no puede estar vacío.")
                    }
                }
            )

            SpinnerEditorial(
                currentEditorials,
                isError = isError,
                selectedEditorial = editorial
            ) { selectedEditorial ->
                editorial = selectedEditorial
                println(editorial)
            }

            Spacer(modifier = Modifier.padding(8.dp)) // Espacio entre elementos.
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(0.9F)
            ) {
                Switch(
                    checked = favorito,
                    onCheckedChange = { favorito = it }
                )
                Spacer(Modifier.width(8.dp))
                Text("¿Es favorito?")
            }

            Spacer(modifier = Modifier.padding(16.dp)) // Espacio entre elementos.
            Button(
                modifier = Modifier.fillMaxWidth(.95F),
                onClick = {
                    if (superName.isNotEmpty() && realName.isNotEmpty() && editorial != 0) {
                        buttonEnabled = false // Deshabilita el botón para evitar múltiples clics.
                        viewModel.saveSuper(
                            oneSuper.copy(
                                superName = superName.trim(),
                                realName = realName.trim(),
                                favorite = favorito,
                                idEditorial = editorial
                            )
                        )
                        navController.popBackStack() // Navega hacia atrás en la pila de navegación.
                    } else isError = true
                },
                enabled = buttonEnabled) {
                Text("Guardar y Volver")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpinnerEditorial(
    editorials: List<Editorial>,
    isError: Boolean = false,
    selectedEditorial: Int,
    onSelect: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selected by remember { mutableIntStateOf(selectedEditorial) }

    val editorialsSorted = editorials.sortedBy { it.name }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth(.95F)
    ) {
        OutlinedTextField(
            value = editorials.find { it.idEd == selectedEditorial }?.name
                ?: "Selecciona una editorial",
            onValueChange = {},
            singleLine = true,
            readOnly = true,
            label = { Text("Editorial") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryEditable, true),
            isError = isError,
            supportingText = {
                if (isError) {
                    Text("Debes seleccionar una editorial.")
                }
            }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            editorialsSorted.forEach { oneEditorial ->
                DropdownMenuItem(
                    text = { Text(oneEditorial.name) },
                    onClick = {
                        selected = oneEditorial.idEd
                        onSelect(selected)
                        expanded = false
                    }
                )
            }
        }
    }
}