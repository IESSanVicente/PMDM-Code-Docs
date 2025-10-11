package es.javiercarrasco.documentationt9_1.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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

@Composable
fun SAFExample() {
    val contxt = LocalContext.current
    var uriString by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    val openDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let {
                uriString = it.toString()
                content =
                    contxt.contentResolver.openInputStream(it)?.bufferedReader().use { reader ->
                        reader?.readText() ?: "Error al leer el archivo"
                    }
            }
        }
    )

    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Storage Access Framework (SAF)",
                style = MaterialTheme.typography.titleMedium
            )

            Button(onClick = {
                // Abrir el selector de archivos para elegir un archivo de texto
                openDocumentLauncher.launch(arrayOf("text/plain"))
            }) {
                Text(text = "Seleccionar archivo de texto")
            }

            OutlinedTextField(
                value = uriString,
                onValueChange = { uriString = it },
                label = { Text("URI del archivo seleccionado") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Contenido del archivo") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                readOnly = true,
                maxLines = 10
            )
        }
    }
}