package es.javiercarrasco.ejemplologin.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import es.javiercarrasco.ejemplologin.R

@Composable
fun LoginDialog(onLogin: (String, String) -> Unit = { _, _ -> }) {
    val ctxt = LocalContext.current
    val openDialog = remember { mutableStateOf(false) }
    var user by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = { openDialog.value = true }) {
            Text(text = ctxt.getString(R.string.txt_login))
        }

        if (openDialog.value) {
            AlertDialog(
                onDismissRequest = { openDialog.value = true }, // Se mantiene el diálogo abierto.
                title = { Text(text = ctxt.getString(R.string.txt_login)) },
                text = {
                    Column {
                        OutlinedTextField(
                            value = user,
                            onValueChange = { user = it },
                            singleLine = true,
                            label = { Text(ctxt.getString(R.string.txt_user)) }
                        )
                        OutlinedTextField(
                            value = pass,
                            onValueChange = { pass = it },
                            singleLine = true,
                            label = { Text(ctxt.getString(R.string.txt_password)) },
                            visualTransformation = PasswordVisualTransformation()
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (user.isNotBlank() && pass.isNotBlank()) {
                            onLogin(user, pass)
                            openDialog.value = false
                            // Limpiar los campos después del inicio de sesión.
                            user = ""
                            pass = ""
                        }
                    }) {
                        Text(ctxt.getString(android.R.string.ok))
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        openDialog.value = false
                        user = ""
                        pass = ""
                    }) {
                        Text(ctxt.getString(android.R.string.cancel))
                    }
                }
            )
        }
    }
}