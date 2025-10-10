package es.javiercarrasco.documentationt8_6

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import es.javiercarrasco.documentationt8_6.ui.theme.DocumentationT8_6Theme
import java.text.DecimalFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            DocumentationT8_6Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GalleryPicker(modifier = Modifier.padding(innerPadding)) { uri: Uri? ->
                        // Aquí puedes manejar el Uri seleccionado (o null si se canceló)
                        if (uri == null) {
                            Toast.makeText(
                                this,
                                "No se ha seleccionado ninguna imagen",
                                Toast.LENGTH_LONG
                            ).show()
                            return@GalleryPicker
                        } else Toast.makeText(this, "Uri: ${uri.path}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}

// Composable para seleccionar una imagen de la galería y mostrar una vista previa.
@Composable
fun GalleryPicker(modifier: Modifier = Modifier, onImageSelected: (Uri?) -> Unit) {
    val contxt = LocalContext.current
    // Se guarda la Uri de forma "saveable" (Uri es Parcelable) para sobrevivir a rotaciones, etc.
    var selectedImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var imageInfo by rememberSaveable { mutableStateOf("") }

    // Lanzador del Photo Picker (selección única)
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri: Uri? ->
            selectedImageUri = uri
            imageInfo = uri?.let { getImageInfo(contxt, it) } ?: ""
            onImageSelected(uri)
        }
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Vista previa con Coil si hay Uri
        AsyncImage(
            model = selectedImageUri,
            contentDescription = "Imagen seleccionada",
            modifier = Modifier
                .size(320.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(4.dp))

        // Información de la imagen
        if (selectedImageUri != null) {
            Spacer(Modifier.height(16.dp))
            Text(
                text = imageInfo,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = {
                // Solo imágenes (puedes cambiar a ImageAndVideo si procede)
                pickImageLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
                // Alternativa para seleccionar cualquier tipo de archivo multimedia:
//                pickImageLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
            }
        ) {
            Text("Elegir de galería")
        }
    }
}

// Obtiene información básica (nombre, tamaño y tipo MIME) de un Uri.
fun getImageInfo(context: Context, uri: Uri): String {
    val contentResolver = context.contentResolver
    val projection = arrayOf(
        android.provider.OpenableColumns.DISPLAY_NAME,
        android.provider.OpenableColumns.SIZE
    )

    contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
        val nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
        val sizeIndex = cursor.getColumnIndex(android.provider.OpenableColumns.SIZE)

        if (cursor.moveToFirst()) {
            val name = cursor.getString(nameIndex)
            val sizeBytes = cursor.getLong(sizeIndex)
            val mime = contentResolver.getType(uri) ?: "Desconocido"

            val sizeFormatted = DecimalFormat("#,##0.00").format(sizeBytes / 1024.0)
            return "📄 $name\n📐 $sizeFormatted KB\n🗂 Tipo: $mime"
        }
    }
    return "Información no disponible"
}