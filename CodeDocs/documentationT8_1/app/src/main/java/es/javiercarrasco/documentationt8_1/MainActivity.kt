package es.javiercarrasco.documentationt8_1

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import es.javiercarrasco.documentationt8_1.ui.theme.DocumentationT81Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DocumentationT81Theme {
                MainScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreen() {
    val ctxt = LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }

    val mediaPlayer = MediaPlayer.create(ctxt, R.raw.epic_cinematic)

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (isPlaying) {
                Button(
                    onClick = {
                        mediaPlayer.pause() // Pausa la reproducción.
                        isPlaying = mediaPlayer.isPlaying
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.pause_24px),
                        contentDescription = "Pause",
                        tint = Color.White
                    )
                }
            } else {
                Button(
                    onClick = {
                        // Crea un nuevo MediaPlayer cada vez que se pulsa el botón.
                        mediaPlayer.start()
                        isPlaying = mediaPlayer.isPlaying
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.White
                    )
                }
            }
            Spacer(Modifier.size(4.dp))
            Button(
                onClick = {
                    if (isPlaying) {
                        mediaPlayer.stop()
                        isPlaying = mediaPlayer.isPlaying
                        mediaPlayer.prepare() // Prepara el MediaPlayer para poder reproducirlo de nuevo.
                        // mediaPlayer.release() // Libera recursos del MediaPlayer.
                    }
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.stop_24px),
                    contentDescription = "Stop",
                    tint = Color.White
                )
            }
        }
    }
}