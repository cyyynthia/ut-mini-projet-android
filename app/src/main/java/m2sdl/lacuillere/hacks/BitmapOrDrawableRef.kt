package m2sdl.lacuillere.hacks

import android.content.Context
import android.graphics.Bitmap
import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize
import androidx.core.graphics.drawable.toBitmap
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import m2sdl.lacuillere.asBitmap
import m2sdl.lacuillere.asCompressedByteArray
import m2sdl.lacuillere.data.repository.RepositoryLocator
import kotlin.ByteArray

@Parcelize
class BitmapOrDrawableRef private constructor(
	@IgnoredOnParcel
	private var bitmap: Bitmap? = null,
	private val drawable: Int? = null,
) : Parcelable {
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

	@Suppress("unused")
	private var compressedBitmap: ByteArray?
		get() = bitmap?.asCompressedByteArray()
		set(value) { bitmap = value?.asBitmap() }

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
		fitIn: DpSize? = null,
		modifier: Modifier = Modifier,
		contentScale: ContentScale = ContentScale.Crop,
	) {
		val ctx = LocalContext.current
		val bitmap by derivedStateOf {
			try {
				val bitmap = toBitmap(ctx)
				fitIn?.let {
					with(Density(ctx)) {
						bitmap.resizeToFitIn(
							it.width.roundToPx(),
							it.height.roundToPx(),
						)
					}
				} ?: bitmap
			} catch (t: Throwable) {
				// Likely fucked smth up in dev
				// Kind of extreme solution but at that point...
				RepositoryLocator.purge(ctx)
				throw t
			}
		}

		Image(
			bitmap.asImageBitmap(),
			contentDescription = contentDescription,
			modifier = modifier,
			contentScale = contentScale,
		)
	}

	private fun Bitmap.resizeToFitIn(targetWidth: Int, targetHeight: Int): Bitmap {
		if (width > height) {
			if (height < targetHeight) return this
			val targetWidth = (width * targetHeight / height)
			return Bitmap.createScaledBitmap(
				this,
				targetWidth,
				targetHeight,
				true
			)
		}

		if (width < targetWidth) return this
		val targetHeight = (height * targetWidth / width)
		return Bitmap.createScaledBitmap(
			this,
			targetWidth,
			targetHeight,
			true
		)
	}
}
