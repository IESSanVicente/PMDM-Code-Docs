package es.javiercarrasco.ejemplost4.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import es.javiercarrasco.ejemplost4.navigation.MyRoutes

@Composable
fun MainScreen(modifier: Modifier = Modifier, navController: NavHostController) {
    val context = LocalContext.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "Pantalla de Inicio"
        )
        FilledTonalButton(
            modifier = Modifier.fillMaxWidth(.85f),
            onClick = {
                navController.navigate(MyRoutes.MyDetailScreen.route)
//                val intent = Intent(context, SecondActivity::class.java)
//                context.startActivity(intent)
            }
        ) {
            Text("Detail Screen")
        }
    }
}