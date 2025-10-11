package es.javiercarrasco.documentationt9_1.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import es.javiercarrasco.documentationt9_1.readPreference
import es.javiercarrasco.documentationt9_1.savePreference

@Composable
fun PreferenceExample() {
    // Ejemplo de uso de la función guardarPreferencia y leerPreferencia
    val contxt = LocalContext.current
    var nombre by remember { mutableStateOf("") }
    var nombreLeido by remember { mutableStateOf("") }

    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Escribe y lee con SharedPreferences",
                style = MaterialTheme.typography.titleSmall
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                placeholder = { Text("Nombre") } // Equivalente a hint en XML
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = nombre.isNotEmpty(),
                onClick = {
                    // Guardar el nombre en las preferencias con la clave "nombre"
                    savePreference(context = contxt, key = "nombre", value = nombre.trim())
                    nombre = ""
                    nombreLeido = ""
                }
            ) {
                Text("Guardar preferencia")
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    // Leer el nombre de las preferencias con la clave "nombre"
                    nombreLeido = readPreference(context = contxt, key = "nombre") ?: "Invitado"
                }
            ) {
                Text("Leer preferencia")
            }
            if (!nombreLeido.isEmpty()) {
                HorizontalDivider()
                Text(
                    text = "Hola, $nombreLeido",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}