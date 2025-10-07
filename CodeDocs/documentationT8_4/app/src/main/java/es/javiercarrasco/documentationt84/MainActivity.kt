package es.javiercarrasco.documentationt84

import android.Manifest
import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : ComponentActivity() {
    // Executor para tareas en segundo plano.
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            // Se inicializa el executor una sola vez.
            cameraExecutor = Executors.newSingleThreadExecutor()
            CameraApp()
        }
    }

    @Composable
    fun CameraApp() {
        val context = LocalContext.current
        var hasPermissions by remember { mutableStateOf(false) }

        // Solicitar permisos.
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { permissions ->
                hasPermissions = permissions.values.all { it }
            }
        )

        LaunchedEffect(Unit) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                launcher.launch(arrayOf(Manifest.permission.CAMERA))
            else {
                launcher.launch(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE // Solo necesario < Q (29)
                    )
                )
            }
        }

        if (hasPermissions) {
            CameraScreen(context = context, executor = cameraExecutor)
        } else {
            RequestPermissionScreen {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                    launcher.launch(arrayOf(Manifest.permission.CAMERA))
                else {
                    launcher.launch(
                        arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE // Solo necesario < Q (29)
                        )
                    )
                }
            }
        }
    }

    @Composable
    private fun RequestPermissionScreen(onRetry: () -> Unit) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Se requiere permiso de cámara", color = Color.Red)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onRetry) {// Reintentar
                    Text("Solicitar permisos")
                }
            }
        }
    }

    @Composable
    private fun CameraScreen(context: android.content.Context, executor: ExecutorService) {
        val imageCaptureState = remember { mutableStateOf<ImageCapture?>(null) }
        val isCameraReady by remember { derivedStateOf { imageCaptureState.value != null } }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(48.dp)
        ) {
            // Vista de previsualización
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val lifecycleOwner = context as LifecycleOwner

                    // Iniciar la cámara después de que la vista esté lista
                    startCamera(
                        context = context,
                        previewView = previewView,
                        lifecycleOwner = lifecycleOwner,
                        onSuccess = { capture ->
                            Log.d("CameraX", "Cámara iniciada correctamente")
                            imageCaptureState.value = capture  // <-- Actualizamos el estado
                        },
                        onError = { e ->
                            Toast.makeText(
                                context,
                                "Error cámara: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )

                    previewView
                },
                modifier = Modifier.weight(1f)
            )

            // Botón para tomar foto (solo si está listo)
            Button(
                onClick = {
                    val capture = imageCaptureState.value
                    if (capture != null) {
                        takePhoto(capture, context, executor) {
                            // Se utiliza el main executor para mostrar el Toast.
                            ContextCompat.getMainExecutor(context).execute {
                                Toast.makeText(context, "Foto guardada", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(context, "Espere… cámara iniciándose", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = isCameraReady, // Deshabilitado hasta que esté listo
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            ) {
                Text(if (isCameraReady) "Hacer Foto" else "Iniciando cámara...")
            }
        }
    }

    private fun startCamera(
        context: android.content.Context,
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner,
        onSuccess: (ImageCapture) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            try {
                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build().apply {
                    setSurfaceProvider(previewView.surfaceProvider)
                }

                val imageCapture = ImageCapture.Builder()
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                    .build()

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                // Se desvincula el provider previo antes de re-vincular
                cameraProvider.unbindAll()

                // Se vincula al ciclo de vida correcto
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture
                )

                // Se notifica que ImageCapture está listo
                onSuccess(imageCapture)

            } catch (e: Exception) {
                onError(e)
            }
        }, ContextCompat.getMainExecutor(context))
    }

    private fun takePhoto(
        imageCapture: ImageCapture,
        context: android.content.Context,
        executor: ExecutorService,
        onSaved: () -> Unit
    ) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "IMG_${System.currentTimeMillis()}")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { // Solo para Android 10+ (Q)
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/CameraX-Compose")
            }
        }

        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            context.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()

        imageCapture.takePicture(
            outputOptions,
            executor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(result: ImageCapture.OutputFileResults) {
                    onSaved()
                }

                override fun onError(exception: ImageCaptureException) {
                    exception.printStackTrace()
                    Log.e("CameraX", "Error al guardar la foto: ${exception.message}", exception)

                    // Se utiliza el main executor para mostrar el Toast.
                    ContextCompat.getMainExecutor(context).execute {
                        Toast.makeText(
                            context,
                            "Error al guardar: ${exception.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        )
    }

    // Se cierra el executor al destruir la actividad.
    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
