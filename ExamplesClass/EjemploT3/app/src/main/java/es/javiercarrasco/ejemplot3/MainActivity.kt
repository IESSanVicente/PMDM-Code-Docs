@file:OptIn(ExperimentalMaterial3Api::class)

package es.javiercarrasco.ejemplot3

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.AlarmClock
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import es.javiercarrasco.ejemplot3.ui.theme.EjemploT3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EjemploT3Theme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { TopAppBar(title = { Text(stringResource(R.string.app_name)) }) }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        OpenUrlExample("https://www.javiercarrasco.es/documentation/pmdm/tema3/")
                        SetAlarmExample(mensaje = "Despertador de prueba", hora = 8, minuto = 30)
//                        CallPhoneExample("123456789")
                        CallPhone("123456789")
                    }
                }
            }
        }
    }
}

// Abrir una URL con el navegador web del dispositivo (Intent implicito).
@Composable
fun OpenUrlExample(url: String) {
    val context = LocalContext.current

    OutlinedButton(
        onClick = {
            // Intent para abrir un navegador web
            Intent(Intent.ACTION_VIEW, url.toUri()).apply {
                // Para utilizar un Intent implicito, es recomendable verificar que exista una actividad
                // que pueda manejar el Intent, de lo contrario la aplicación puede fallar. (Queries)
                if (this.resolveActivity(context.packageManager) != null)
                    context.startActivity(this)
                else showToast(context, "Hay un problema para encontrar un navegador.")
            }
        },
        modifier = Modifier
            .padding(8f.dp)
            .fillMaxWidth()
    ) {
        Text("Abrir navegador")
    }
}

// Establecer una alarma en el dispositivo (Intent implicito).
@Composable
fun SetAlarmExample(mensaje: String, hora: Int, minuto: Int) {
    val context = LocalContext.current

    OutlinedButton(
        onClick = {
            // Código para establecer una alarma
            val intent = Intent(AlarmClock.ACTION_SET_ALARM)
            intent.putExtra(AlarmClock.EXTRA_MESSAGE, mensaje)
            intent.putExtra(AlarmClock.EXTRA_HOUR, hora)
            intent.putExtra(AlarmClock.EXTRA_MINUTES, minuto)

            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            } else showToast(context, "Hay un problema para establecer la alarma.")
        },
        modifier = Modifier
            .padding(8f.dp)
            .fillMaxWidth()
    ) {
        Text("Establecer alarma")
    }
}

@Composable
fun CallPhone(phoneNumber: String) {
    val context = LocalContext.current
    OutlinedButton(
        onClick = {
            // Código para realizar una llamada telefónica
            Intent(Intent.ACTION_CALL).apply {
                data = "tel:$phoneNumber".toUri()

                addCategory(Intent.CATEGORY_DEFAULT) // Añade categoría por defecto
                flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK // Asegura que se abra en una nueva tarea

                if (this.resolveActivity(context.packageManager) != null) {
                    context.startActivity(this)
                } else showToast(context, "Hay un problema para realizar la llamada.")
            }
        },
        modifier = Modifier
            .padding(8f.dp)
            .fillMaxWidth()
    ) {
        Text("Llamar")
    }
}

// Realizar una llamada telefónica (Intent implicito). Requiere permiso CALL_PHONE.
// Añadir la dependencia en build.gradle: androidx.lifecycle:lifecycle-viewmodel-compose:2.9.4
@Composable
fun CallPhoneExample(phoneNumber: String, viewModel: PermissionHandlerViewModel = viewModel()) {
    val context = LocalContext.current
    val permissionState = viewModel.uiState.collectAsStateWithLifecycle() // Estado de los permisos

    // Callback para solicitar el permiso de llamada.
    val requestPermission =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            viewModel.onPermissionResult( // Actualiza el estado de los permisos en el ViewModel.
                granted, ActivityCompat.shouldShowRequestPermissionRationale(
                    context as Activity, Manifest.permission.CALL_PHONE
                )
            )
        }

    // Observamos el estado del permiso y actuamos en consecuencia.
    LaunchedEffect(permissionState) {
        when {
            permissionState.value.granted -> {
                // Aquí abrimos la cámara; por simplicidad indicamos con un log
                Log.d("CameraPermission", "Acceso a cámara concedido")
                // Podrías lanzar una navegación o mostrar vista de cámara
            }

            else -> {
                // Primer lanzamiento: solicitamos el permiso
                requestPermission.launch(Manifest.permission.CALL_PHONE)
            }
        }
    }

    when {
        permissionState.value.granted -> {
            OutlinedButton(
                onClick = {
                    // Código para realizar una llamada telefónica
                    Intent(Intent.ACTION_CALL).apply {
                        data = "tel:$phoneNumber".toUri()

                        addCategory(Intent.CATEGORY_DEFAULT) // Añade categoría por defecto
                        flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK // Asegura que se abra en una nueva tarea

                        if (this.resolveActivity(context.packageManager) != null) {
                            context.startActivity(this)
                        } else showToast(context, "Hay un problema para realizar la llamada.")
                    }
                },
                modifier = Modifier
                    .padding(8f.dp)
                    .fillMaxWidth()
            ) {
                Text("Llamar")
            }
        }

        permissionState.value.showRationale -> {
            Text(
                text = "Se necesita acceso para realizar llamadas telefónicas",
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
            )
            // Solicitar nuevamente el permiso.
            Button(
                onClick = { // Se solicita el permiso de llamada telefónica.
                    requestPermission.launch(Manifest.permission.CALL_PHONE)
                },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Solicitar permiso")
            }

            showToast(context, "Es necesario tener acceso para realizar llamadas telefónicas")
        }

        permissionState.value.permanentlyDenied -> { // Permiso denegado permanentemente
            Text(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                text = "Permiso denegado. Habilítalo en los ajustes de la aplicación."
            )
            Button(
                onClick = { // Abre los ajustes de la aplicación para conceder el permiso manualmente.
                    context.startActivity(
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                    )
                }, modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text("Abrir ajustes")
            }
        }
    }
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}