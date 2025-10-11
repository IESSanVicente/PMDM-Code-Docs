package es.javiercarrasco.documentationt9_1.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import es.javiercarrasco.documentationt9_1.readThemeMode
import es.javiercarrasco.documentationt9_1.saveThemeMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun DataStoreExample() {
    val contxt = LocalContext.current
    var checked by remember { mutableStateOf(false) }

    // Leer el modo tema desde DataStore
    LaunchedEffect(checked) {
        readThemeMode(contxt).collect { valor ->
            checked = valor
        }
    }

    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Escribe y lee con DataStore",
                style = MaterialTheme.typography.titleSmall
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Switch(
                    checked = checked,
                    onCheckedChange = {
                        checked = it

                        // Guardar el modo tema en DataStore
                        CoroutineScope(Dispatchers.IO).launch {
                            saveThemeMode(contxt, it)
                        }
                    }
                )
                Text(
                    if (checked) "Tema oscuro" else "Tema claro",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            HorizontalDivider()
            Text(
                text = "Estado del tema: ${if (checked) "Oscuro" else "Claro"}",
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}