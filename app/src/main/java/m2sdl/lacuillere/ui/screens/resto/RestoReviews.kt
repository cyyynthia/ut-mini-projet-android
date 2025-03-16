package m2sdl.lacuillere.ui.screens.resto

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.os.BundleCompat
import m2sdl.lacuillere.PhotoActivity
import m2sdl.lacuillere.asBitmap
import m2sdl.lacuillere.data.Restaurant
import m2sdl.lacuillere.ui.composables.rememberActivityLauncherForResult

@Composable
fun ReviewsTab(restaurant: Restaurant) {
	var photo by remember { mutableStateOf<Bitmap?>(null) }

	val photoActivityLauncher = rememberActivityLauncherForResult(PhotoActivity::class.java) {
		photo = it?.extras?.let { BundleCompat.getParcelable(it, "photo", ByteArray::class.java) }?.asBitmap()
	}

	Column {
		Button(onClick = photoActivityLauncher) {
			Text("Photo")
		}

		if (photo != null) {
			Image(photo!!.asImageBitmap(), contentDescription = null)
		}
	}
}
