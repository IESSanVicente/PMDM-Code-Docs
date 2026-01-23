package es.javiercarrasco.ejemplologin.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import es.javiercarrasco.ejemplologin.MainViewModel
import es.javiercarrasco.ejemplologin.R
import es.javiercarrasco.ejemplologin.data.model.LoginRequest
import es.javiercarrasco.ejemplologin.data.model.LoginState
import es.javiercarrasco.ejemplologin.ui.components.LoginDialog

// Pantalla principal que gestiona el estado de login y muestra la UI correspondiente.
@Composable
fun HomeScreen(viewModel: MainViewModel = viewModel()) {
    val loginState by viewModel.loginState.collectAsStateWithLifecycle()
    val coffeeList by viewModel.stateCoffee.collectAsStateWithLifecycle()
    val ctxt = LocalContext.current

    // Se obtiene la sesión al iniciar el Composable, se ejecuta cada vez que cambia el
    // estado de login para verificar si el usuario ya está logueado.
    LaunchedEffect(loginState) {
        viewModel.getSessionFlow()
    }

    // Se muestra la UI según el estado de login.
    when (loginState) {
        is LoginState.Error -> {
            val message = (loginState as LoginState.Error).message
            Toast.makeText(
                ctxt,
                ctxt.getString(R.string.txt_login_error, message),
                Toast.LENGTH_LONG
            ).show()
        }

        is LoginState.Success -> {
            // Se obtienen los cafés si el login es correcto.
            viewModel.getCoffees()
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(text = "Número de cafés recuperados: ${coffeeList.size}")
                Spacer(modifier = Modifier.width(10.dp))
                // Botón para cerrar sesión.
                Button(onClick = { viewModel.logout() }) {
                    Text(text = ctxt.getString(R.string.txt_logout))
                }
            }
        }

        is LoginState.Idle -> {
            // Se muestra el diálogo de inicio de sesión
            LoginDialog(
                onLogin = { user, pass ->
                    // Aquí se maneja la lógica de inicio de sesión
                    viewModel.login(
                        LoginRequest(user, pass)
                    )
                }
            )
        }

        is LoginState.Loading -> {
            // Aquí se podría mostrar un indicador de carga si lo deseas
            Text(text = ctxt.getString(R.string.txt_login_loading))
        }
    }
}