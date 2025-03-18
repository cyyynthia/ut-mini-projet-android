package m2sdl.lacuillere.data.repository

import m2sdl.lacuillere.data.Reservation

class ReservationRepository(initialData: MutableList<Reservation>?) :
	AbstractRepository<Reservation>(initialData ?: mutableListOf(*BASE_DATA)) {

	companion object {
		val BASE_DATA = arrayOf<Reservation>()
	}
}
