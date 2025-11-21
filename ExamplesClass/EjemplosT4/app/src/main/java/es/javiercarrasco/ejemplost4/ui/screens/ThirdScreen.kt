package es.javiercarrasco.ejemplost4.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import es.javiercarrasco.ejemplost4.SharedViewModel
import es.javiercarrasco.ejemplost4.navigation.MyRoutes

@Composable
fun ThirdScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    val context = LocalContext.current

//    val datos = navBackStackEntry.arguments?.getString("datos")

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Third Screen",
        )

        val text =
            if (sharedViewModel.texto.value.isEmpty()) "no hay datos" else sharedViewModel.texto.value
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = text
        )

        Button(onClick = {
            navController.navigate(MyRoutes.MyHomeScreen.route) {
                popUpTo(MyRoutes.MyHomeScreen.route) {
                    inclusive = true
                }
            }
        }) {
            Text("Main Screen")
        }
    }
}