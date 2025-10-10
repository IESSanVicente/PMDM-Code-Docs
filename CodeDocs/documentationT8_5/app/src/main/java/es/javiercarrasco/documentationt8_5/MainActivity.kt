package es.javiercarrasco.documentationt8_5

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.FallbackStrategy
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                Scaffold(modifier = Modifier.padding(8.dp)) { innerPadding ->
                    CameraRecorderScreen(innerPadding)
                }
            }
        }
    }
}

@Composable
fun CameraRecorderScreen(innerPadding: PaddingValues) {
    val contxt = LocalContext.current // Contexto necesario para varias llamadas.
    val lifecycleOwner =
        LocalLifecycleOwner.current // Necesario para vincular el ciclo de vida a CameraX.

    // Gestión de permisos, estado simple para el ejemplo.
    var hasCamera by remember { mutableStateOf(false) }
    var hasMic by remember { mutableStateOf(false) }

    val permissionsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        hasCamera = result[Manifest.permission.CAMERA] == true
        hasMic = result[Manifest.permission.RECORD_AUDIO] == true
    }

    // Se lanza la petición de permisos al inicio, solo una vez (key1 = Unit).
    LaunchedEffect(Unit) {
        permissionsLauncher.launch(
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            )
        )
    }

    // Se crea previewView para la cámara, se usa remember para que no se recree en recomposiciones.
    val previewView = remember {
        PreviewView(contxt).apply { // PreviewView es un View de Android.
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            scaleType = PreviewView.ScaleType.FILL_CENTER
        }
    }

    // cameraProviderFuture es un proceso asíncrono, se guarda en remember para que no se reinicie.
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(contxt) }
    // videoCapture se guarda como estado para poder usarlo en los botones.
    var videoCapture by remember { mutableStateOf<VideoCapture<Recorder>?>(null) }
    // isRecording para controlar el estado de grabación y deshabilitar botones.
    var isRecording by remember { mutableStateOf(false) }
    // lastVideoUri para mostrar la última ruta guardada en Galería.
    var lastVideoUri by remember { mutableStateOf<String?>(null) }

    // Executor principal para callbacks de CameraX
    val mainExecutor = remember { ContextCompat.getMainExecutor(contxt) }

    // Configurar CameraX una vez que el CameraProvider esté disponible.
    LaunchedEffect(cameraProviderFuture) {
        // Listener asíncrono, se lanza cuando cameraProviderFuture está listo.
        cameraProviderFuture.addListener({
            // CameraProvider listo, se configuran casos de uso.
            val cameraProvider = cameraProviderFuture.get()

            val previewUseCase = Preview.Builder().build()
                .also { it.setSurfaceProvider(previewView.surfaceProvider) }

            // Configuración de grabación con calidad y fallback.
            // Se puede ajustar la lista de calidades según necesidades.
            val qualitySelector = QualitySelector.fromOrderedList(
                listOf(Quality.UHD, Quality.FHD, Quality.HD, Quality.SD),
                FallbackStrategy.lowerQualityOrHigherThan(Quality.SD)
            )

            // Recorder con el selector de calidad.
            val recorder = Recorder.Builder()
                .setQualitySelector(qualitySelector)
                .build()

            // Caso de uso de VideoCapture
            val videoUseCase = VideoCapture.withOutput(recorder)

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    previewUseCase,
                    videoUseCase
                )
                videoCapture = videoUseCase
            } catch (e: Exception) {
                Log.e("CameraX", "Fallo al vincular casos de uso", e)
            }
        }, mainExecutor)
    }

    // Pantalla UI.
    Box(
        Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        // Vista previa
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )

        // Controles
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!hasCamera) {
                Text("Concede permiso de cámara para empezar.")
                Spacer(Modifier.height(8.dp))
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    enabled = !isRecording && hasCamera && videoCapture != null,
                    onClick = {
                        val vc = videoCapture ?: return@Button

                        // 1) Crear el registro de salida en MediaStore (Galería)
                        val name = "CameraX-${
                            SimpleDateFormat(
                                "yyyyMMdd-HHmmss",
                                Locale.US
                            ).format(System.currentTimeMillis())
                        }.mp4"
                        val contentValues = ContentValues().apply {
                            put(MediaStore.Video.Media.DISPLAY_NAME, name)
                            put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
                            // Ruta visible en Galería (Android 10+)
                            put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/CameraX Recorder")
                        }
                        val mediaStoreOptions = MediaStoreOutputOptions.Builder(
                            contxt.contentResolver,
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        ).setContentValues(contentValues).build()

                        // 2) Preparar la grabación
                        var pending = vc.output.prepareRecording(contxt, mediaStoreOptions)

                        // Habilitar audio solo si el permiso está concedido
                        if (hasMic) {
                            if (ActivityCompat.checkSelfPermission(
                                    contxt,
                                    Manifest.permission.RECORD_AUDIO
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                pending = pending.withAudioEnabled()
                            }
                        }

                        // 3) Iniciar y escuchar eventos
                        val recording = pending.start(mainExecutor) { event ->
                            when (event) {
                                is VideoRecordEvent.Start -> isRecording = true

                                is VideoRecordEvent.Finalize -> {
                                    isRecording = false
                                    if (event.error == VideoRecordEvent.Finalize.ERROR_NONE) {
                                        lastVideoUri = event.outputResults.outputUri.toString()
                                        // El archivo ya queda indexado en la Galería
                                    } else Log.e("CameraX", "Error al finalizar: ${event.error}")
                                }
                            }
                        }

                        // Guardamos el handle en estado recordable para poder parar más tarde
                        RecordingHolder.recording = recording
                    }
                ) {
                    Text("Grabar")
                }

                Button(
                    enabled = isRecording,
                    onClick = {
                        // 4) Detener grabación
                        RecordingHolder.recording?.stop()
                        RecordingHolder.recording?.close()
                        RecordingHolder.recording = null
                    }
                ) {
                    Text("Detener")
                }
            }

            Spacer(Modifier.height(8.dp))
            if (lastVideoUri != null) {
                Text(
                    text = "Guardado en Galería:\n$lastVideoUri",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

// Holder para el Recording activo (fuera del ViewModel para simplicidad del ejemplo)
private object RecordingHolder {
    var recording: Recording? = null
}