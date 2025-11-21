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
import androidx.navigation.NavHostController
import es.javiercarrasco.ejemplost4.SharedViewModel
import es.javiercarrasco.ejemplost4.navigation.MyRoutes

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    val context = LocalContext.current
    var name by rememberSaveable { mutableStateOf("") }

    BackHandler { // Capturamos evento back button
        if (name.isEmpty())
            Toast.makeText(context, "No puedes volver", Toast.LENGTH_SHORT).show()
        else navController.popBackStack()
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Detail Screen",
        )
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Tu nombre") }
        )

        Button(
            onClick = {
                navController.popBackStack()
            },
            enabled = name.isNotEmpty()
        ) {
            Text("Volver")
        }

        Button(onClick = {
            sharedViewModel.updateNombre(name)
            navController.navigate(
                MyRoutes.MyThirdScreen.route
            )
        }) {
            Text("Tercer Screen")
        }
    }
}