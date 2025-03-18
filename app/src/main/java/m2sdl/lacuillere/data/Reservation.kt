package m2sdl.lacuillere.data

import kotlinx.parcelize.Parcelize
import java.util.Date
import java.util.UUID

@Parcelize
data class Reservation(
	val userId: UUID,
	val restaurantId: UUID,
	val peopleCount: Int,
	val date: Date,
) : Entity()
