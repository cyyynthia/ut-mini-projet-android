package m2sdl.lacuillere.data

import android.graphics.Bitmap
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Review(
	val userId: UUID,
	val restaurantId: UUID,
	val note: Int,
	val text: String,
	val photos: List<Bitmap>,
) : Entity()
