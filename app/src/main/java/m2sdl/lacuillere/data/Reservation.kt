package m2sdl.lacuillere.data

import java.util.Date
import java.util.UUID

data class Reservation(
	val userId: UUID,
	val restaurantId: UUID,
	val peopleCount: Int,
	val date: Date,
) : Entity()
