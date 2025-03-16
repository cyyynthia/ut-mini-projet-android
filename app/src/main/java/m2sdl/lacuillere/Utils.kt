package m2sdl.lacuillere

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.common.util.concurrent.ListenableFuture
import com.google.maps.android.compose.CameraPositionState
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.io.ByteArrayOutputStream
import java.util.UUID
import java.util.concurrent.Executor

fun checkPermissions(ctx: Context, vararg permissions: String): Boolean {
	return permissions.any { ContextCompat.checkSelfPermission(ctx, it) == PackageManager.PERMISSION_GRANTED }
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

// https://github.com/perracodex/Kotlinx-UUID-Serializer

object UUIDSerializer : KSerializer<UUID> {
	override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

	override fun serialize(encoder: Encoder, value: UUID) = encoder.encodeString(value.toString())

	override fun deserialize(decoder: Decoder): UUID = UUID.fromString(decoder.decodeString())
}

typealias SUUID = @Serializable(with = UUIDSerializer::class) UUID
