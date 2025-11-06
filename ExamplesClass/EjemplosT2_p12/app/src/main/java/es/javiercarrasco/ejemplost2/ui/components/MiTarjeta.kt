package es.javiercarrasco.ejemplost2.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import es.javiercarrasco.ejemplost2.R

@Composable
fun MiTarjeta(usuario: String, onClick: (String) -> Unit = {}, onClickInfo: (String) -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick(usuario)
            }
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_contact),
                contentDescription = "Imagen de usuario",
                modifier = Modifier
                    .wrapContentSize()
                    .size(50.dp)
            )
            Text(
                text = usuario,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .weight(0.25f),
                style = MaterialTheme.typography.bodyLarge
            )
            Icon(
                Icons.Rounded.Info,
                contentDescription = "Icono de información",
                modifier = Modifier.clickable {
                    onClickInfo(usuario)
                }
            )
        }
    }
}