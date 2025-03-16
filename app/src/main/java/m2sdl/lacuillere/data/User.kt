package m2sdl.lacuillere.data

import android.graphics.Bitmap

data class User(
	val name: String,
	val avatar: Bitmap
) : Entity()
