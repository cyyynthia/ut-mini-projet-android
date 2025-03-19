package m2sdl.lacuillere.data

import kotlinx.parcelize.Parcelize
import m2sdl.lacuillere.hacks.BitmapOrDrawableRef
import java.util.UUID

@Parcelize
data class Review(
	override val id: UUID = UUID.randomUUID(),
	val userId: UUID,
	val restaurantId: UUID,
	val note: Float,
	val text: String,
	val photos: List<BitmapOrDrawableRef>,
) : Entity
