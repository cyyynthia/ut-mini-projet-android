package m2sdl.lacuillere.data

import kotlinx.parcelize.Parcelize
import java.util.Date
import java.util.UUID

@Parcelize
data class Reservation(
	override val id: UUID = UUID.randomUUID(),
	val userId: UUID,
	val restaurantId: UUID,
	// Some of there are theoretically redundant, but the data layer of the app is chaotic so it is what it is, lmao
	// Could just pull things from the user object eh...
	val name: String,
	val phone: String,
	val peopleCount: Int,
	val date: Date,
) : Entity
