package m2sdl.lacuillere.hacks

import android.content.Context
import android.graphics.Bitmap
import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.toBitmap
import kotlinx.parcelize.Parcelize

@Parcelize
class BitmapOrDrawableRef private constructor(private val bitmap: Bitmap?, private val drawable: Int?) : Parcelable {
	constructor(bitmap: Bitmap) : this(bitmap, null)
	constructor(@DrawableRes drawable: Int) : this(null, drawable)

	fun toBitmap(ctx: Context): Bitmap {
		bitmap?.let { return it }
		drawable?.let { return ctx.resources.getDrawable(it, null).toBitmap() }
		throw IllegalStateException()
	}
}
