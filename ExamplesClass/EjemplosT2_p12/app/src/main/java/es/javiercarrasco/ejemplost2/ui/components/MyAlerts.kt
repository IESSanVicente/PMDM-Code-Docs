package es.javiercarrasco.ejemplost2.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun AlertDialogExample(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector = Icons.Default.Warning,
) {
    AlertDialog(
        icon = { Icon(icon, contentDescription = "Example Icon") },
        title = { Text(text = dialogTitle) },
        text = { Text(text = dialogText) },
        properties = DialogProperties(
            dismissOnBackPress = false, // Se evita el cierre al presionar atrás.
            dismissOnClickOutside = false // Se evita el cierre al tocar fuera del diálogo.
        ),
        onDismissRequest = { onDismissRequest() }, // Se llama a la función de cierre del diálogo si no está bloqueado el cierre.
        confirmButton = {
            TextButton(onClick = { onConfirmation() }) {
                Text(LocalContext.current.getString(android.R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text(LocalContext.current.getString(android.R.string.cancel))
            }
        }
    )
}


@Composable
fun CustomDialog(onSave: (String) -> Unit) {
    val abierto: MutableState<Boolean> = remember { mutableStateOf(false) }
    var texto by remember { mutableStateOf("") }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { abierto.value = true }) {
            Text("Nuevo elemento")
        }

        if (abierto.value) {
            Dialog(onDismissRequest = { abierto.value = false }) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        modifier = Modifier.padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Column(
                            modifier = Modifier.wrapContentSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Spacer(Modifier.height(16.dp))
                            Text("Crear nuevo elemento")
                            OutlinedTextField(
                                label = { Text("Introduce un nombre") },
                                value = texto,
                                onValueChange = { texto = it },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(0.9f) // Ajusta el ancho del campo de texto (%)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                            ) {
                                TextButton(
                                    onClick = { abierto.value = false; texto = "" },
                                    modifier = Modifier.padding(8.dp),
                                ) {
                                    Text(LocalContext.current.getString(android.R.string.cancel))
                                }
                                TextButton(
                                    onClick = {
                                        if (texto.isNotBlank()) {
                                            onSave(texto)
                                            abierto.value = false
                                            texto = ""
                                        }
                                    },
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    Text("Guardar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogoSeleccionHora() {
    var mostrarDialogo by remember { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState() // Hora actual del sistema.
    var horaSeleccionada by remember { mutableStateOf("") }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { mostrarDialogo = true }) {
            Text("Seleccionar hora")
        }

        Spacer(Modifier.height(8.dp))
        Text(text = "Hora seleccionada: $horaSeleccionada")

        if (mostrarDialogo) {
            AlertDialog(
                onDismissRequest = { mostrarDialogo = false },
                confirmButton = {
                    TextButton(onClick = {
                        val h = timePickerState.hour.toString().padStart(2, '0')
                        val m = timePickerState.minute.toString().padStart(2, '0')
                        horaSeleccionada = "$h:$m"
                        mostrarDialogo = false
                    }) { Text("Aceptar") }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarDialogo = false }) {
                        Text("Cancelar")
                    }
                },
                title = { Text("Selecciona la hora") },
                text = { TimePicker(state = timePickerState) }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogoSeleccionFecha() {
    var mostrarDialogo by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState() // Fecha actual del sistema.
    var fechaSeleccionada by remember { mutableStateOf("") }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { mostrarDialogo = true }) {
            Text("Seleccionar fecha")
        }

        Spacer(Modifier.height(8.dp))
        Text("Fecha seleccionada: $fechaSeleccionada")

        if (mostrarDialogo) {
            AlertDialog(
                onDismissRequest = { mostrarDialogo = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                            val fecha =
                                Instant.ofEpochMilli(millis) // Convertir milisegundos a Instant.
                                    .atZone(ZoneId.systemDefault()) // Convertir a zona horaria local.
                                    .toLocalDate() // Obtener la fecha local.
                            fechaSeleccionada = formatter.format(fecha)
                        }
                        mostrarDialogo = false
                    }) { Text("Aceptar") }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarDialogo = false }) {
                        Text("Cancelar")
                    }
                },
                title = { Text("Selecciona la fecha") },
                text = { DatePicker(state = datePickerState) }
            )
        }
    }
}

@Composable
fun BasicDialog(
    user: String,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            onDismissRequest()
        }, // Se cierra el diálogo al tocar fuera de él.
        title = { Text(user) },
        text = { Text("Información sobre el usuario $user.") },
        confirmButton = {
            TextButton(onClick = { onConfirmation() }) {
                Text(LocalContext.current.getString(android.R.string.ok))
            }
        }
    )
}