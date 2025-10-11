package es.javiercarrasco.documentationt9_1.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SecurePrefsScreen(vm: SecurePrefsViewModel) {
    val token by vm.token.collectAsState()
    val encrypted by vm.encrypted.collectAsState()

    var input by remember { mutableStateOf("") }
    val isValid = input.isNotBlank()

    Card {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Almacenamiento seguro (Keystore + AES-GCM + DataStore)",
                style = MaterialTheme.typography.titleSmall
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = input,
                onValueChange = { input = it },
                label = { Text("Token / API key") },
                singleLine = true
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    modifier = Modifier.fillMaxWidth(.5f),
                    enabled = isValid,
                    onClick = { vm.saveToken(input.trim()) }) { Text("Guardar seguro") }
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { vm.clearToken() }) { Text("Eliminar") }
            }
            HorizontalDivider()
            Text(
                "Valor actual (descifrado): ${token ?: "—"}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                "Valor actual (cifrado, Base64): ${encrypted ?: "—"}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}
