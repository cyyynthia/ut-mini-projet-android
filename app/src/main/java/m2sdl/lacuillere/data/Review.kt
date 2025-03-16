package m2sdl.lacuillere.data

import android.graphics.Bitmap
import java.util.UUID

data class Review(
	val userId: UUID,
	val restaurantId: UUID,
	val note: Int,
	val text: String,
	val photos: List<Bitmap>,
) : Entity()
