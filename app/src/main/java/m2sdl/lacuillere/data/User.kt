package m2sdl.lacuillere.data

import android.graphics.Bitmap
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
	val name: String,
	val avatar: Bitmap
) : Entity()
