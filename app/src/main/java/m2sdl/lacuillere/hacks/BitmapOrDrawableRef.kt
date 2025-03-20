package m2sdl.lacuillere.hacks

import android.content.Context
import android.graphics.Bitmap
import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
class BitmapOrDrawableRef private constructor(private val bitmap: Bitmap?, private val drawable: Int?) : Parcelable {
	companion object {
		fun Bitmap.toHackyBitmap(): BitmapOrDrawableRef {
			return BitmapOrDrawableRef(this)
		}

		fun Iterable<Bitmap>.toHackyBitmapList(): List<BitmapOrDrawableRef> {
			return map { it.toHackyBitmap() }
		}
	}

	constructor(bitmap: Bitmap) : this(bitmap, null)
	constructor(@DrawableRes drawable: Int) : this(null, drawable)

	@IgnoredOnParcel
	private lateinit var cachedBitmap: Bitmap

	fun toBitmap(ctx: Context): Bitmap {
		if (!this::cachedBitmap.isInitialized) {
			cachedBitmap = bitmap
				?: drawable?.let { ctx.resources.getDrawable(it, null).toBitmap() }
				?: throw IllegalStateException()
		}

		return cachedBitmap
	}

	@Composable
	fun HackyImage(
		contentDescription: String?,
		modifier: Modifier = Modifier,
		contentScale: ContentScale = ContentScale.Crop,
	) {
		val ctx = LocalContext.current
		val bitmap = toBitmap(ctx)

		Image(
			bitmap.asImageBitmap(),
			contentDescription = contentDescription,
			modifier = modifier,
			contentScale = contentScale,
		)
	}
}
