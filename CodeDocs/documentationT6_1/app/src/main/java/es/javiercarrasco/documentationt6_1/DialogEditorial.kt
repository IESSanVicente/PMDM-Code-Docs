package es.javiercarrasco.documentationt6_1

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog


@Composable
fun DialogEditorial(
    onDismiss: () -> Unit,
    onAccept: (String) -> Unit = { _ -> } // Función para manejar la acción de aceptar, por defecto no hace nada.
) {
    // Aquí se implementaría el contenido del diálogo para editar o añadir editoriales.
    // Por ejemplo, se podría usar un AlertDialog con campos de texto para el nombre de la editorial.
    // Este es un ejemplo básico, se puede personalizar según las necesidades de la aplicación.
    // Se puede usar un ViewModel para manejar la lógica de negocio y la interacción con la base de datos.

    var nombreEditorial by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = { false } // Se bloquea el cierre del diálogo al pulsar fuera de él.
    ) {
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
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(16.dp))
                    Text("Añadir Editorial")

                    OutlinedTextField(
                        value = nombreEditorial,
                        onValueChange = { nombreEditorial = it },
                        label = { Text("Nombre de la Editorial") },
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        isError = isError,
                        supportingText = {
                            if (isError) {
                                Text("El nombre de la editorial no puede estar vacío.")
                            }
                        }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        TextButton(
                            onClick = { onDismiss() },
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Text("Cancelar")
                        }
                        TextButton(
                            onClick = {
                                if (nombreEditorial.isNotBlank()){
                                    onAccept(nombreEditorial) // Llamar a la función de aceptar con el nombre de la editorial.
                                    isError = false // Reiniciar el estado de error.
                                    nombreEditorial = "" // Limpiar el campo después de guardar.
                                    onDismiss()
                                } else {
                                    isError = true // Mostrar error si el campo está vacío.
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
