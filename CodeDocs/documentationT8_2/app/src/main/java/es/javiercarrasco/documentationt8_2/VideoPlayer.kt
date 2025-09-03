package es.javiercarrasco.documentationt8_2

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

// VideoPlayer.kt
@Composable
fun VideoPlayer(videoUrl: String) {
    val ctxt = LocalContext.current

    // Se crea la instancia de ExoPlayer.
    val exoPlayer = remember {
        ExoPlayer.Builder(ctxt).build().apply {
            val mediaItem = MediaItem.fromUri(videoUrl)
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }
    }

    // Integración de la vista nativa de Android (PlayerView) en Compose.
    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
                useController = true // muestra controles
            }
        },
        modifier = Modifier.fillMaxWidth(),
        onRelease = { playerView ->
            playerView.player?.release()
        }
    )
}