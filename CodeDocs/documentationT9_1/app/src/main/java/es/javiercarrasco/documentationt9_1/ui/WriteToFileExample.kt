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
import es.javiercarrasco.documentationt9_1.readFromFile
import es.javiercarrasco.documentationt9_1.saveToFile

@Composable
fun WriteToFileExample() {
    // Ejemplo de uso de la función guardarArchivo
    val contxt = LocalContext.current
    var textoToFile by remember { mutableStateOf("") }
    var textoFromFile by remember { mutableStateOf("") }

    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Escribe en un archivo de texto en memoria interna",
                style = MaterialTheme.typography.titleSmall
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = textoToFile,
                onValueChange = { textoToFile = it },
                label = { Text("Texto para el fichero") },
                placeholder = { Text("Texto para el fichero") } // Equivalente a hint en XML
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = textoToFile.isNotEmpty(),
                onClick = {
                    // Guardar el texto en un archivo llamado "miArchivo.txt"
                    // El texto se añade al final del archivo si ya existe, si no existe, se crea
                    saveToFile(
                        context = contxt,
                        fileName = "miArchivo.txt",
                        contenido = "${textoToFile.trim()}\n"
                    )
                    textoToFile = ""
                    textoFromFile = ""
                }
            ) {
                Text("Guardar en archivo")
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    // Guardar el texto en un archivo llamado "miArchivo.txt"
                    // El texto se añade al final del archivo si ya existe, si no existe, se crea
                    textoFromFile = readFromFile(context = contxt, fileName = "miArchivo.txt")
                }
            ) {
                Text("Leer del archivo")
            }
            if (!textoFromFile.isEmpty()) {
                HorizontalDivider()
                Text(
                    text = "Contenido del archivo:",
                    style = MaterialTheme.typography.titleSmall
                )

                Text(
                    text = textoFromFile,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}