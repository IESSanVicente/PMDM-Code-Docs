package es.javiercarrasco.documentationt8_7

import android.Manifest
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import es.javiercarrasco.documentationt8_7.ui.theme.DocumentationT8_7Theme
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DocumentationT8_7Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AudioRecorderScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AudioRecorderScreen(modifier: Modifier = Modifier) {
    val contxt = LocalContext.current
    var isRecording by remember { mutableStateOf(false) }
    var elapsedTime by remember { mutableStateOf(0L) }
    var recorder by remember { mutableStateOf<MediaRecorder?>(null) }
    var outputUri by remember { mutableStateOf<Uri?>(null) }

    // Lanzador para solicitar permisos
    val requestPermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted)
            Toast.makeText(contxt, "Permiso denegado", Toast.LENGTH_SHORT).show()
    }

    // Se lanza la petición de permisos al inicio, solo una vez (key1 = Unit).
    LaunchedEffect(Unit) {
        requestPermission.launch(Manifest.permission.RECORD_AUDIO)
    }

    // Cronómetro
    LaunchedEffect(isRecording) {
        if (isRecording) {
            elapsedTime = 0
            while (isActive && isRecording) {
                delay(1000)
                elapsedTime++
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Grabadora de audio", style = MaterialTheme.typography.titleLarge)
        Text("Tiempo: ${formatTime(elapsedTime)}")

        Button(
            onClick = {
                if (isRecording) {
                    recorder?.stop()
                    recorder?.release()
                    recorder = null
                    isRecording = false
                    Toast.makeText(contxt, "Grabación finalizada", Toast.LENGTH_SHORT).show()
                } else {
                    try {
                        val (uri, fd) = createMediaStoreOutput(contxt)
                        outputUri = uri

                        recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            MediaRecorder(contxt)
                        } else {
                            MediaRecorder()
                        }.apply {
                            setAudioSource(MediaRecorder.AudioSource.MIC)
                            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                            setAudioEncodingBitRate(128000)
                            setAudioSamplingRate(44100)
                            setOutputFile(fd.fileDescriptor)
                            prepare()
                            start()
                        }
                        isRecording = true
                    } catch (e: Exception) {
                        Toast.makeText(
                            contxt,
                            "Error: ${e.localizedMessage}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        ) {
            Text(if (isRecording) "Detener" else "Grabar")
        }

        outputUri?.let {
            Text("Archivo guardado en:\n$it", style = MaterialTheme.typography.bodySmall)
        }
    }
}

// Crea un archivo de salida en MediaStore y devuelve su URI y descriptor de archivo.
private fun createMediaStoreOutput(context: Context): Pair<Uri, ParcelFileDescriptor> {
    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val name = "REC_$timestamp.m4a"

    val values = ContentValues().apply {
        put(MediaStore.Audio.Media.DISPLAY_NAME, name)
        put(MediaStore.Audio.Media.MIME_TYPE, "audio/mp4")
        put(MediaStore.Audio.Media.RELATIVE_PATH, "${Environment.DIRECTORY_MUSIC}/PMDM")
    }

    val resolver: ContentResolver = context.contentResolver
    val uri = resolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values)
        ?: throw IllegalStateException("No se pudo crear el archivo en MediaStore")

    val pfd = resolver.openFileDescriptor(uri, "w")
        ?: throw IllegalStateException("No se pudo abrir el descriptor de archivo")

    return uri to pfd
}

// Formatea el tiempo en segundos a mm:ss
private fun formatTime(seconds: Long): String {
    val m = seconds / 60
    val s = seconds % 60
    return "%02d:%02d".format(m, s)
}