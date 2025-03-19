package m2sdl.lacuillere

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.common.util.concurrent.ListenableFuture
import com.google.maps.android.compose.CameraPositionState
import java.io.ByteArrayOutputStream
import java.util.concurrent.Executor

fun checkPermissions(ctx: Context, vararg permissions: String): Boolean {
	return permissions.any { ContextCompat.checkSelfPermission(ctx, it) == PackageManager.PERMISSION_GRANTED }
}

fun Context.toast(text: String, duration: Int = Toast.LENGTH_LONG) {
	Toast.makeText(this, text, duration).show()
}

fun Context.notImplementedToast() {
	toast("Cette fonctionnalité n'est pas implémentée.", Toast.LENGTH_SHORT)
}

fun CameraPositionState.isNull() = position.target.latitude == 0.0 && position.target.longitude == 0.0

fun LatLng.applyTo(cps: CameraPositionState) {
	cps.position = CameraPosition.fromLatLngZoom(this, 15f)
}

// Write things in a more Kotlin-friendly way
fun <V> ListenableFuture<V>.addListener(executor: Executor, fn: () -> Unit) = this.addListener(fn, executor)

// "implements Parcelable" my ass
fun Bitmap.asCompressedByteArray(): ByteArray {
	val os = ByteArrayOutputStream()
	compress(Bitmap.CompressFormat.JPEG, 90, os)
	return os.toByteArray()
}

fun ByteArray.asBitmap(): Bitmap {
	return BitmapFactory.decodeByteArray(this, 0, this.size)
}
