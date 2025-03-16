package m2sdl.lacuillere.data.repository

import m2sdl.lacuillere.data.Reservation

class ReservationRepository : AbstractRepository<Reservation>() {
	override val database = mutableListOf(*BASE_DATA)

	companion object {
		val BASE_DATA = arrayOf<Reservation>()
	}
}
