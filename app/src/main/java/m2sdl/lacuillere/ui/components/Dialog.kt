package m2sdl.lacuillere.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.window.Dialog
import m2sdl.lacuillere.hacks.BitmapOrDrawableRef

@Composable
fun BigImageDialog(image: BitmapOrDrawableRef, onClose: () -> Unit) {
	Dialog(onDismissRequest = onClose) {
		Box(modifier = Modifier.fillMaxSize()) {
			image.HackyImage(
				contentScale = ContentScale.None,
				contentDescription = null,
				modifier = Modifier
					.fillMaxSize()
					.clickable { onClose() }
			)
		}
	}
}
